package com.apposum.githubrepos.entity

sealed class Data<RequestData>(var data: RequestData? = null) {
    class Error<RequestData>(var error: com.apposum.githubrepos.entity.Error, data: RequestData? = null): Data<RequestData>(data)
    class Success<RequestData>(data: RequestData? = null): Data<RequestData>(data)
    class Loading<RequestData>(data: RequestData? = null): Data<RequestData>(data)
}