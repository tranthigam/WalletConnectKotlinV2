package com.walletconnect.sign.engine.use_case.calls

import com.walletconnect.android.internal.common.exception.CannotFindSequenceForTopic
import com.walletconnect.android.internal.common.model.IrnParams
import com.walletconnect.android.internal.common.model.Tags
import com.walletconnect.android.internal.common.model.type.JsonRpcInteractorInterface
import com.walletconnect.android.internal.utils.DAY_IN_SECONDS
import com.walletconnect.android.internal.utils.WEEK_IN_SECONDS
import com.walletconnect.foundation.common.model.Topic
import com.walletconnect.foundation.common.model.Ttl
import com.walletconnect.foundation.util.Logger
import com.walletconnect.sign.common.exceptions.NO_SEQUENCE_FOR_TOPIC_MESSAGE
import com.walletconnect.sign.common.exceptions.NotSettledSessionException
import com.walletconnect.sign.common.exceptions.SESSION_IS_NOT_ACKNOWLEDGED_MESSAGE
import com.walletconnect.sign.common.exceptions.UNAUTHORIZED_EXTEND_MESSAGE
import com.walletconnect.sign.common.exceptions.UnauthorizedPeerException
import com.walletconnect.sign.common.model.vo.clientsync.session.SignRpc
import com.walletconnect.sign.common.model.vo.clientsync.session.params.SignParams
import com.walletconnect.sign.storage.sequence.SessionStorageRepository
import kotlinx.coroutines.supervisorScope

internal class ExtendSessionUseCase(
    private val jsonRpcInteractor: JsonRpcInteractorInterface,
    private val sessionStorageRepository: SessionStorageRepository,
    private val logger: Logger,
) : ExtendSessionUseCaseInterface {

    override suspend fun extend(topic: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) = supervisorScope {
        if (!sessionStorageRepository.isSessionValid(Topic(topic))) {
            return@supervisorScope onFailure(CannotFindSequenceForTopic("$NO_SEQUENCE_FOR_TOPIC_MESSAGE$topic"))
        }

        val session = sessionStorageRepository.getSessionWithoutMetadataByTopic(Topic(topic))
        if (!session.isAcknowledged) {
            return@supervisorScope onFailure(NotSettledSessionException("$SESSION_IS_NOT_ACKNOWLEDGED_MESSAGE$topic"))
        }

        val newExpiration = session.expiry.seconds + WEEK_IN_SECONDS
        sessionStorageRepository.extendSession(Topic(topic), newExpiration)
        val sessionExtend = SignRpc.SessionExtend(params = SignParams.ExtendParams(newExpiration))
        val irnParams = IrnParams(Tags.SESSION_EXTEND, Ttl(DAY_IN_SECONDS))

        jsonRpcInteractor.publishJsonRpcRequest(
            Topic(topic), irnParams, sessionExtend,
            onSuccess = {
                logger.log("Session extend sent successfully")
                onSuccess()
            },
            onFailure = { error ->
                logger.error("Sending session extend error: $error")
                onFailure(error)
            })
    }
}

internal interface ExtendSessionUseCaseInterface {
    suspend fun extend(topic: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit)
}