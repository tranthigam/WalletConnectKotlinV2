package com.walletconnect.auth.client

interface AuthInterface {

    interface RequesterDelegate {
        fun onAuthResponse(authResponse: Auth.Events.AuthResponse)
    }

    interface ResponderDelegate {
        fun onAuthRequest(authRequest: Auth.Events.AuthRequest)
    }

    fun setRequesterDelegate(delegate: RequesterDelegate)

    fun setResponderDelegate(delegate: ResponderDelegate)

    fun initialize(init: Auth.Params.Init, onError: (Auth.Model.Error) -> Unit)

    fun pair(pair: Auth.Params.Pair, onError: (Auth.Model.Error) -> Unit)

    fun request(params: Auth.Params.Request)

    fun respond(params: Auth.Params.Respond)

    fun getPendingRequest(): Map<Int, Auth.Model.PendingRequest>

    fun getResponse(params: Auth.Params.RequestId): Auth.Model.Response
}