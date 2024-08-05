package dev.blackoutburst.sve.maths

import kotlin.math.sqrt

class Vector2i {
    var x: Int
    var y: Int

    constructor() {
        this.x = 0
        this.y = 0
    }

    constructor(size: Int) {
        this.x = size
        this.y = size
    }

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun normalize(): Vector2i {
        val mag = sqrt((x * x + y * y).toDouble()).toFloat()
        if (mag != 0f) {
            x = (x / mag).toInt()
            y = (y / mag).toInt()
        }

        return (this)
    }

    fun mul(v: Float): Vector2i {
        x = (x * v).toInt()
        y = (y * v).toInt()

        return (this)
    }

    fun length(): Float {
        return (sqrt((x * x + y * y).toDouble()).toFloat())
    }

    fun copy(): Vector2i {
        val newVector = Vector2i()
        newVector.x = this.x
        newVector.y = this.y

        return (newVector)
    }

    override fun toString(): String {
        return "[$x, $y]"
    }
}
