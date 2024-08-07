package dev.blackoutburst.sve.utils

fun String.fit(length: Int): String {
    var newString = this.toAscii()

    if (newString.length == length) return newString

    if (newString.length > length) newString = newString.substring(0, length)

    for (x in newString.length until length)
        newString += '\u0000'

    return newString
}

fun String.toAscii(): String {
    var newString = ""
    for (c in this.toCharArray()) {
        if (c.code < 128)
            newString += c
    }

    return newString
}