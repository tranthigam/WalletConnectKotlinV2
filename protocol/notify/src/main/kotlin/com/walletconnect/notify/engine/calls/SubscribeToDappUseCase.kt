@file:JvmSynthetic

package com.walletconnect.notify.engine.calls

import android.net.Uri
import com.walletconnect.android.internal.common.crypto.kmr.KeyManagementRepository
import com.walletconnect.android.internal.common.crypto.sha256
import com.walletconnect.android.internal.common.model.AccountId
import com.walletconnect.android.internal.common.model.AppMetaDataType
import com.walletconnect.android.internal.common.model.DidJwt
import com.walletconnect.android.internal.common.model.EnvelopeType
import com.walletconnect.android.internal.common.model.IrnParams
import com.walletconnect.android.internal.common.model.Participants
import com.walletconnect.android.internal.common.model.Tags
import com.walletconnect.android.internal.common.model.params.CoreNotifyParams
import com.walletconnect.android.internal.common.model.type.JsonRpcInteractorInterface
import com.walletconnect.android.internal.common.storage.MetadataStorageRepositoryInterface
import com.walletconnect.android.internal.utils.THIRTY_SECONDS
import com.walletconnect.foundation.common.model.PublicKey
import com.walletconnect.foundation.common.model.Topic
import com.walletconnect.foundation.common.model.Ttl
import com.walletconnect.foundation.util.Logger
import com.walletconnect.notify.common.model.NotifyRpc
import com.walletconnect.notify.engine.domain.ExtractMetadataFromConfigUseCase
import com.walletconnect.notify.engine.domain.ExtractPublicKeysFromDidJsonUseCase
import com.walletconnect.notify.engine.domain.FetchDidJwtInteractor
import kotlinx.coroutines.supervisorScope

typealias DidJsonPublicKeyPair = Pair<PublicKey, PublicKey>

internal class SubscribeToDappUseCase(
    private val jsonRpcInteractor: JsonRpcInteractorInterface,
    private val crypto: KeyManagementRepository,
    private val extractMetadataFromConfigUseCase: ExtractMetadataFromConfigUseCase,
    private val metadataStorageRepository: MetadataStorageRepositoryInterface,
    private val fetchDidJwtInteractor: FetchDidJwtInteractor,
    private val extractPublicKeysFromDidJson: ExtractPublicKeysFromDidJsonUseCase,
    private val logger: Logger,
) : SubscribeToDappUseCaseInterface {

    override suspend fun subscribeToDapp(dappUri: Uri, account: String, onSuccess: (Long, DidJwt) -> Unit, onFailure: (Throwable) -> Unit) = supervisorScope {
        //TODO: Those errors are not caught and handled
        val (dappPublicKey, authenticationPublicKey) = extractPublicKeysFromDidJson(dappUri).getOrThrow()
        val (dappMetaData, dappScopes) = extractMetadataFromConfigUseCase(dappUri).getOrThrow()

        val subscribeTopic = Topic(sha256(dappPublicKey.keyAsBytes))


        val selfPublicKey = crypto.generateAndStoreX25519KeyPair()
        val responseTopic = crypto.generateTopicFromKeyAgreement(selfPublicKey, dappPublicKey)

        val didJwt = fetchDidJwtInteractor.subscriptionRequest(AccountId(account), authenticationPublicKey, dappUri.toString(), dappScopes.map { it.id })
            .getOrElse { error -> return@supervisorScope onFailure(error) }

        val params = CoreNotifyParams.SubscribeParams(didJwt.value)
        val request = NotifyRpc.NotifySubscribe(params = params)
        val irnParams = IrnParams(Tags.NOTIFY_SUBSCRIBE, Ttl(THIRTY_SECONDS))

        runCatching<Unit> {
            metadataStorageRepository.insertOrAbortMetadata(
                topic = responseTopic,
                appMetaData = dappMetaData,
                appMetaDataType = AppMetaDataType.PEER,
            )
        }.onFailure { error ->
            logger.error("Cannot insert: ${error.message}")
            return@supervisorScope onFailure(error)
        }

        jsonRpcInteractor.subscribe(responseTopic) { error -> onFailure(error) }

        jsonRpcInteractor.publishJsonRpcRequest(
            topic = subscribeTopic,
            params = irnParams,
            payload = request,
            envelopeType = EnvelopeType.ONE,
            participants = Participants(selfPublicKey, dappPublicKey),
            onSuccess = { onSuccess(request.id, didJwt) },
            onFailure = { error -> onFailure(error) }
        )
    }
}

internal interface SubscribeToDappUseCaseInterface {

    suspend fun subscribeToDapp(
        dappUri: Uri,
        account: String,
        onSuccess: (Long, DidJwt) -> Unit,
        onFailure: (Throwable) -> Unit,
    )
}