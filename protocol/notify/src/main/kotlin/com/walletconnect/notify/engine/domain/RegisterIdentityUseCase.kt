@file:JvmSynthetic

package com.walletconnect.notify.engine.domain

import com.walletconnect.android.internal.common.model.AccountId
import com.walletconnect.android.internal.common.signing.cacao.Cacao
import com.walletconnect.android.keyserver.domain.IdentitiesInteractor
import com.walletconnect.foundation.common.model.PublicKey
import kotlinx.coroutines.supervisorScope

internal class RegisterIdentityUseCase(
    private val identitiesInteractor: IdentitiesInteractor,
    private val identityServerUrl: String,
) {
    suspend operator fun invoke(
        accountId: AccountId, domain: String, isLimited: Boolean, onSign: (String) -> Cacao.Signature?, onSuccess: suspend (PublicKey) -> Unit, onFailure: (Throwable) -> Unit,
    ) = supervisorScope {
        identitiesInteractor
            .registerIdentity(accountId, if (isLimited) LIMITED_STATEMENT else UNLIMITED_STATEMENT, domain, listOf(identityServerUrl), identityServerUrl, onSign)
            .fold(onFailure = onFailure, onSuccess = { identityPublicKey -> onSuccess(identityPublicKey) })
    }

    companion object {
        private const val LIMITED_STATEMENT =
            "I further authorize this app to send and receive messages on my behalf for THIS domain using my WalletConnect identity. Read more at https://walletconnect.com/identity"
        private const val UNLIMITED_STATEMENT =
            "I further authorize this app to send and receive messages on my behalf for ALL domains using my WalletConnect identity. Read more at https://walletconnect.com/identity"
    }
}
