package dev.blackoutburst.sve.maths

import kotlin.math.sqrt

class Vector3b {
    var x: Byte
    var y: Byte
    var z: Byte

    constructor() {
        this.x = 0
        this.y = 0
        this.z = 0
    }

    constructor(size: Byte) {
        this.x = size
        this.y = size
        this.z = size
    }

    constructor(x: Byte, y: Byte, z: Byte) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun set(x: Byte, y: Byte, z: Byte) {
        this.x = x
        this.y = y
        this.z = z
    }


    fun length(): Float {
        return (sqrt((x * x + y * y + z * z).toDouble()).toFloat())
    }

    override fun toString(): String {
        return "[$x, $y, $z]"
    }
}
