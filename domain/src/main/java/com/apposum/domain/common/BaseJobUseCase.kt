package com.apposum.domain.common

import com.apposum.domain.entity.DataEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext

abstract class BaseJobUseCase<in D, T>(private val coroutineContext: CoroutineContext){

    abstract fun getDataChannel(data: D? = null): ReceiveChannel<DataEntity<T>>

    abstract fun sendToPresentation(data: DataEntity<T>): DataEntity<T>

    fun produce(withData: D? = null): ReceiveChannel<DataEntity<T>> {

        return GlobalScope.produce(context = coroutineContext) {
            val dataChannel = getDataChannel(withData)
            dataChannel.consumeEach {
                send(sendToPresentation(it))
            }
        }
    }
}
