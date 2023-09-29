package com.walletconnect.web3.modal.di

import com.walletconnect.android.internal.common.di.AndroidCommonDITags
import com.walletconnect.web3.modal.data.BalanceRpcRepository
import com.walletconnect.web3.modal.data.network.BalanceService
import com.walletconnect.web3.modal.domain.usecase.GetEthBalanceUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BALANCE_RPC_RETROFIT = "balanceRpcRetrofit"
internal fun balanceRpcModule() = module {

    single(named(BALANCE_RPC_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl("https://rpc.walletconnect.com/v1/")
            .client(get(named(AndroidCommonDITags.OK_HTTP)))
            .addConverterFactory(MoshiConverterFactory.create(get(named(AndroidCommonDITags.MOSHI))))
            .build()
    }

    single { get<Retrofit>(named(BALANCE_RPC_RETROFIT)).create(BalanceService::class.java) }

    single { BalanceRpcRepository(balanceService = get()) }

    single { GetEthBalanceUseCase(balanceRpcRepository = get(), logger = get()) }
}