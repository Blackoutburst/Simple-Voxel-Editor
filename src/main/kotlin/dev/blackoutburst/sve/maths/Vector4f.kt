package dev.blackoutburst.sve.maths

import kotlin.math.sqrt

class Vector4f {
    var x: Float
    var y: Float
    var z: Float
    var w: Float

    constructor() {
        this.x = 0.0f
        this.y = 0.0f
        this.z = 0.0f
        this.w = 0.0f
    }

    constructor(size: Float) {
        this.x = size
        this.y = size
        this.z = size
        this.w = size
    }

    constructor(x: Float, y: Float, z: Float, w: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun set(x: Float, y: Float, z: Float, w: Float): Vector4f {
        this.x = x
        this.y = y
        this.z = z
        this.w = w

        return this
    }

    fun normalize(): Vector4f {
        val mag = sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat()
        if (mag != 0f) {
            x /= mag
            y /= mag
            z /= mag
            w /= mag
        }

        return (this)
    }

    fun mul(v: Float): Vector4f {
        x *= v
        y *= v
        z *= v
        w *= v

        return (this)
    }

    fun length(): Float {
        return (sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat())
    }

    fun copy(): Vector4f {
        val newVector = Vector4f()
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
