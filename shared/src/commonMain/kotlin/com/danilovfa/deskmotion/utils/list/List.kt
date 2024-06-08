package com.danilovfa.deskmotion.utils.list

fun <T>List<T>.compress(maxSize: Int): List<T> {
    val jumpCount = (this.size / maxSize).takeUnless { it == 0 } ?: 1
    val compressedList = mutableListOf<T>()
    var i = 0
    while (i < this.size) {
        compressedList.add(this[i])
        i += jumpCount
    }
    return compressedList
}