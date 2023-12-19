package com.danilovfa.deskmotion.library.lce

sealed class Lce<out C> : LceState {

    override val isLoading: Boolean get() = this is Loading
    override val isContent: Boolean get() = this is Content
    override val isError: Boolean get() = this is Error

    fun contentOrNull(): C? = (this as? Content)?.value
    override fun errorOrNull(): Throwable? = (this as? Error)?.error

    object Loading : Lce<Nothing>(), LceState.Loading {
        override fun toString(): String = "Loading"
    }

    data class Content<C>(val value: C) : Lce<C>(), LceState.Content
    data class Error(override val error: Throwable) : Lce<Nothing>(), LceState.Error

    companion object {
        fun <C> of(content: C): Lce<C> = Content(content)
    }
}

inline fun <T, R> Lce<T>.mapContent(map: (T) -> R): Lce<R> {
    return when (this) {
        is Lce.Loading -> this
        is Lce.Error -> this
        is Lce.Content -> Lce.of(map.invoke(value))
    }
}
