package com.walletconnect.sign.di

import com.walletconnect.android.internal.common.di.AndroidCommonDITags
import com.walletconnect.sign.engine.use_case.ApproveSessionUseCase
import com.walletconnect.sign.engine.use_case.DisconnectSessionUseCase
import com.walletconnect.sign.engine.use_case.EmitEventUseCase
import com.walletconnect.sign.engine.use_case.ExtendSessionUsesCase
import com.walletconnect.sign.engine.use_case.GetPairingsUseCase
import com.walletconnect.sign.engine.use_case.GetSessionProposalsUseCase
import com.walletconnect.sign.engine.use_case.GetSessionsUseCase
import com.walletconnect.sign.engine.use_case.PairUseCase
import com.walletconnect.sign.engine.use_case.PingUseCase
import com.walletconnect.sign.engine.use_case.ProposeSessionUseCase
import com.walletconnect.sign.engine.use_case.RejectSessionUseCase
import com.walletconnect.sign.engine.use_case.RespondSessionRequestUseCase
import com.walletconnect.sign.engine.use_case.SessionRequestUseCase
import com.walletconnect.sign.engine.use_case.SessionUpdateUseCase
import com.walletconnect.sign.json_rpc.domain.GetPendingJsonRpcHistoryEntryByIdUseCase
import com.walletconnect.sign.json_rpc.domain.GetPendingRequestsUseCaseByTopic
import org.koin.core.qualifier.named
import org.koin.dsl.module

@JvmSynthetic
internal fun callsModule() = module {

    single { ProposeSessionUseCase(get(), get(), get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { PairUseCase(get()) }

    single { ApproveSessionUseCase(get(), get(), get(), get(), get(), get(), get()) }

    single { RejectSessionUseCase(get(), get(), get()) }

    single { SessionUpdateUseCase(get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { SessionRequestUseCase(get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { RespondSessionRequestUseCase(get(), get(), get(), get(named(AndroidCommonDITags.LOGGER)), get()) }

    single { PingUseCase(get(), get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { EmitEventUseCase(get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { ExtendSessionUsesCase(get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { DisconnectSessionUseCase(get(), get(), get(named(AndroidCommonDITags.LOGGER))) }

    single { GetSessionsUseCase(get(), get(), get()) }

    single { GetPairingsUseCase(get()) }

    single { GetPendingRequestsUseCaseByTopic(get(), get(), get()) }

    single { GetPendingJsonRpcHistoryEntryByIdUseCase(get(), get()) }

    single { GetSessionProposalsUseCase(get()) }
}