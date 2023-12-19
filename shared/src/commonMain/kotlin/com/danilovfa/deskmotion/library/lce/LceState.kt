package com.danilovfa.deskmotion.library.lce

sealed interface LceState {

    val isLoading: Boolean
    val isContent: Boolean
    val isError: Boolean

    fun errorOrNull(): Throwable?

    sealed interface Loading : LceState

    sealed interface Content : LceState

    sealed interface Error : LceState {
        val error: Throwable
    }

    companion object {
        val Loading: Loading = Lce.Loading
        val Content: Content = Lce.Content(Unit)
        fun Error(error: Throwable): Error = Lce.Error(error)
    }
}

fun Lce<*>.ignoreContent(): LceState {
    return when (this) {
        is Lce.Loading -> this
        is Lce.Error -> this
        is Lce.Content -> Lce.Content(Unit)
    }
}
