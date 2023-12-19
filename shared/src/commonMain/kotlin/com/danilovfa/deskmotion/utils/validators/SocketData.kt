package com.danilovfa.deskmotion.utils.validators

fun isIpValid(ipAddress: String): Boolean {
    val parts = ipAddress.split(".")
    if (parts.size != 4) {
        return false
    }

    for (part in parts) {
        try {
            val num = part.toInt()
            if (num < 0 || num > 255 || num.toString() != part) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }

    return true
}

fun isPortValid(port: String): Boolean {
    return port.toIntOrNull() in 0..65535
}