package com.apposum.domain.entity

sealed class DataEntity<RequestData>(var data: RequestData? = null) {
    class Error<RequestData>(var error: ErrorEntity, data: RequestData? = null): DataEntity<RequestData>(data)
    class Success<RequestData>(data: RequestData? = null): DataEntity<RequestData>(data)
}