package dev.blackoutburst.sve.maths

import kotlin.math.sqrt

class Vector4i {
    var x: Int
    var y: Int
    var z: Int
    var w: Int

    constructor() {
        this.x = 0
        this.y = 0
        this.z = 0
        this.w = 0
    }

    constructor(size: Int) {
        this.x = size
        this.y = size
        this.z = size
        this.w = size
    }

    constructor(x: Int, y: Int, z: Int, w: Int) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun set(x: Int, y: Int, z: Int, w: Int) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun normalize(): Vector4i {
        val mag = sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat()
        if (mag != 0f) {
            x = (x / mag).toInt()
            y = (y / mag).toInt()
            z = (z / mag).toInt()
            w = (w / mag).toInt()
        }

        return (this)
    }

    fun mul(v: Float): Vector4i {
        x = (x * v).toInt()
        y = (y * v).toInt()
        z = (z * v).toInt()
        w = (w * v).toInt()

        return (this)
    }

    fun length(): Float {
        return (sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat())
    }

    fun copy(): Vector4i {
        val newVector = Vector4i()
        newVector.x = this.x
        newVector.y = this.y
        newVector.z = this.z
        newVector.w = this.w

        return (newVector)
    }

    override fun toString(): String {
        return "[$x, $y, $z, $w]"
    }
}
