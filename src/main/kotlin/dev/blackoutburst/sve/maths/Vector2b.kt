package dev.blackoutburst.sve.maths

import kotlin.math.sqrt

class Vector2b {
    var x: Byte
    var y: Byte

    constructor() {
        this.x = 0
        this.y = 0
    }

    constructor(size: Byte) {
        this.x = size
        this.y = size
    }

    constructor(x: Byte, y: Byte) {
        this.x = x
        this.y = y
    }

    fun set(x: Byte, y: Byte) {
        this.x = x
        this.y = y
    }


    fun length(): Float {
        return (sqrt((x * x + y * y).toDouble()).toFloat())
    }

    override fun toString(): String {
        return "[$x, $y]"
    }
}
