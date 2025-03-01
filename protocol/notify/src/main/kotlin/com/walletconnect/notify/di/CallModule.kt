@file:JvmSynthetic

package com.walletconnect.notify.di

import com.walletconnect.android.internal.common.di.AndroidCommonDITags
import com.walletconnect.notify.engine.calls.DecryptMessageUseCase
import com.walletconnect.notify.engine.calls.DecryptMessageUseCaseInterface
import com.walletconnect.notify.engine.calls.DeleteMessageUseCase
import com.walletconnect.notify.engine.calls.DeleteMessageUseCaseInterface
import com.walletconnect.notify.engine.calls.DeleteSubscriptionUseCase
import com.walletconnect.notify.engine.calls.DeleteSubscriptionUseCaseInterface
import com.walletconnect.notify.engine.calls.GetListOfActiveSubscriptionsUseCase
import com.walletconnect.notify.engine.calls.GetListOfActiveSubscriptionsUseCaseInterface
import com.walletconnect.notify.engine.calls.GetListOfMessagesUseCase
import com.walletconnect.notify.engine.calls.GetListOfMessagesUseCaseInterface
import com.walletconnect.notify.engine.calls.GetNotificationTypesUseCase
import com.walletconnect.notify.engine.calls.GetNotificationTypesUseCaseInterface
import com.walletconnect.notify.engine.calls.RegisterUseCase
import com.walletconnect.notify.engine.calls.RegisterUseCaseInterface
import com.walletconnect.notify.engine.calls.SubscribeToDappUseCase
import com.walletconnect.notify.engine.calls.SubscribeToDappUseCaseInterface
import com.walletconnect.notify.engine.calls.UnregisterUseCase
import com.walletconnect.notify.engine.calls.UnregisterUseCaseInterface
import com.walletconnect.notify.engine.calls.UpdateSubscriptionRequestUseCase
import com.walletconnect.notify.engine.calls.UpdateSubscriptionRequestUseCaseInterface
import org.koin.core.qualifier.named
import org.koin.dsl.module

@JvmSynthetic
internal fun callModule() = module {

    single<SubscribeToDappUseCaseInterface> {
        SubscribeToDappUseCase(
            jsonRpcInteractor = get(),
            crypto = get(),
            extractMetadataFromConfigUseCase = get(),
            metadataStorageRepository = get(),
            fetchDidJwtInteractor = get(),
            extractPublicKeysFromDidJson = get(),
            logger = get()
        )
    }

    single<UpdateSubscriptionRequestUseCaseInterface> {
        UpdateSubscriptionRequestUseCase(
            jsonRpcInteractor = get(),
            subscriptionRepository = get(),
            metadataStorageRepository = get(),
            fetchDidJwtInteractor = get()
        )
    }

    single<DeleteSubscriptionUseCaseInterface> {
        DeleteSubscriptionUseCase(
            jsonRpcInteractor = get(),
            metadataStorageRepository = get(),
            subscriptionRepository = get(),
            messagesRepository = get(),
            fetchDidJwtInteractor = get()
        )
    }

    single<DeleteMessageUseCaseInterface> {
        DeleteMessageUseCase(
            messagesRepository = get()
        )
    }

    single<DecryptMessageUseCaseInterface> {
        DecryptMessageUseCase(
            codec = get(),
            serializer = get(),
            jsonRpcHistory = get()
        )
    }

    single<RegisterUseCaseInterface> {
        RegisterUseCase(
            registerIdentityUseCase = get(),
            registeredAccountsRepository = get(),
            watchSubscriptionsUseCase = get()
        )
    }

    single<UnregisterUseCaseInterface> {
        UnregisterUseCase(
            registeredAccountsRepository = get(),
            stopWatchingSubscriptionsUseCase = get(),
            identitiesInteractor = get(),
            keyserverUrl = get(named(AndroidCommonDITags.KEYSERVER_URL)),
            subscriptionRepository = get(),
            messagesRepository = get(),
            jsonRpcInteractor = get()
        )
    }

    single<GetNotificationTypesUseCaseInterface> {
        GetNotificationTypesUseCase(
            getNotifyConfigUseCase = get()
        )
    }

    single<GetListOfActiveSubscriptionsUseCaseInterface> {
        GetListOfActiveSubscriptionsUseCase(
            subscriptionRepository = get(),
            metadataStorageRepository = get()
        )
    }

    single<GetListOfMessagesUseCaseInterface> {
        GetListOfMessagesUseCase(
            messagesRepository = get()
        )
    }
}