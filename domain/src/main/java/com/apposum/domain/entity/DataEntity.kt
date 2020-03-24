package com.apposum.domain.entity

sealed class DataEntity<RequestData> {
    class Error<RequestData>(var error: ErrorEntity, var data: RequestData? = null): DataEntity<RequestData>()
    class Success<RequestData>(var data: RequestData? = null): DataEntity<RequestData>()
    class Loading<RequestData>(var data: RequestData? = null): DataEntity<RequestData>()
}