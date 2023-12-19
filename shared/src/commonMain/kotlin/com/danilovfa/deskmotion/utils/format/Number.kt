package com.danilovfa.deskmotion.utils.format

fun Double.toFormattedString(): String {
    return String.format("%.3f", this)
}