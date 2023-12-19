package com.danilovfa.deskmotion.library.lce

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

const val LCE_FLOW_TAG = "LCE_FLOW"

typealias LceFlow<T> = Flow<Lce<T>>
typealias LceStateFlow = Flow<LceState>

inline fun <T> lceFlow(crossinline block: suspend FlowCollector<T>.() -> Unit): LceFlow<T> {
    return flow { block() }.mapToLce()
}

inline fun lceStateFlow(crossinline block: suspend FlowCollector<Unit>.() -> Unit): LceStateFlow {
    return flow {
        block()
        emit(Unit)
    }.mapToLce()
}

fun <T> Flow<T>.mapToLce(): LceFlow<T> {
    return map<T, Lce<T>> { Lce.Content(it) }
        .onStart { emit(Lce.Loading) }
        .catch { throwable ->
            Logger.e(LCE_FLOW_TAG, throwable)
            emit(Lce.Error(throwable))
        }
}

@JvmName("mapToLceUnit")
fun Flow<Unit>.mapToLce(): LceStateFlow {
    return map<Unit, LceState> { Lce.Content(Unit) }
        .onStart { emit(Lce.Loading) }
        .catch { throwable ->
            Logger.e(LCE_FLOW_TAG, throwable)
            emit(Lce.Error(throwable))
        }
}

private inline fun <T : LceState, reified R> Flow<T>.onEachLceType(crossinline onType: suspend (R) -> Unit): Flow<T> {
    return onEach { lce -> if (lce is R) onType(lce) }
}

fun <T : LceState> Flow<T>.onEachLoading(onLoading: suspend () -> Unit): Flow<T> {
    return onEachLceType<T, LceState.Loading> { onLoading() }
}

fun <T : LceState> Flow<T>.onEachError(onError: suspend (Throwable) -> Unit): Flow<T> {
    return onEachLceType<T, LceState.Error> { onError(it.error) }
}

fun LceStateFlow.onEachContent(onContent: suspend () -> Unit): Flow<LceState> {
    return onEachLceType<LceState, LceState.Content> { onContent() }
}

fun <T> LceFlow<T>.onEachContent(onContent: suspend (T) -> Unit): LceFlow<T> {
    return onEachLceType<Lce<T>, Lce.Content<T>> { onContent(it.value) }
}

inline fun <T, R> LceFlow<T>.mapLceContent(crossinline transform: suspend (T) -> R): LceFlow<R> {
    return map { lce -> lce.mapContent { transform(it) } }
}
