package dev.blackoutburst.sve.utils

import java.lang.Math.clamp

class Color {
    var r: Float
    var g: Float
    var b: Float
    var a: Float

    init {
        this.r = 0.0f
        this.g = 0.0f
        this.b = 0.0f
        this.a = 1.0f
    }

    constructor(c: Float) {
        this.r = c
        this.g = c
        this.b = c
        this.a = 1.0f
    }

    constructor(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = 1.0f
    }

    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    fun darker(): Color {
        r - 0.1f
        g - 0.1f
        b - 0.1f

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    fun lighter(): Color {
        r + 0.1f
        g + 0.1f
        b + 0.1f

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    fun add(c: Color): Color {
        r + c.r
        g + c.g
        b + c.b

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    fun sub(c: Color): Color {
        r - c.r
        g - c.g
        b - c.b

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    fun mul(c: Color): Color {
        r * c.r
        g * c.g
        b * c.b

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    fun copy(): Color = Color(this.r, this.g, this.b, this.a)

    fun div(c: Color): Color {
        if (c.r != 0f) r /= c.r
        if (c.g != 0f) g /= c.g
        if (c.b != 0f) b /= c.b

        clamp(r, 0f, 1f)
        clamp(g, 0f, 1f)
        clamp(b, 0f, 1f)

        return (this)
    }

    override fun toString(): String {
        return "[$r, $g, $b, $a]"
    }

    fun mix(color: Color, factor: Float): Color {
        return Color(
            r = this.r * (1 - factor) + color.r * factor,
            g = this.g * (1 - factor) + color.g * factor,
            b = this.b * (1 - factor) + color.b * factor,
            a = this.a * (1 - factor) + color.a * factor
        )
    }

    companion object {
        val WHITE: Color = Color(1.0f)
        val BLACK: Color = Color(0.0f)
        val GRAY: Color = Color(0.5f)
        val LIGHT_GRAY: Color = Color(0.75f)
        val DARK_GRAY: Color = Color(0.25f)
        val RED: Color = Color(1.0f, 0.0f, 0.0f)
        val GREEN: Color = Color(0.0f, 1.0f, 0.0f)
        val BLUE: Color = Color(0.0f, 0.0f, 1.0f)
        val YELLOW: Color = Color(1.0f, 1.0f, 0.0f)
        val MAGENTA: Color = Color(1.0f, 0.0f, 1.0f)
        val CYAN: Color = Color(0.0f, 1.0f, 1.0f)
        val ORANGE: Color = Color(1.0f, 0.5f, 0.0f)
        val LIGHT_BLUE: Color = Color(0.0f, 0.5f, 1.0f)
        val PURPLE: Color = Color(0.5f, 0.0f, 1.0f, 1.0f)
        val TRANSPARENT: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)

        fun hueToRGB(hue: Float): Color {
            val h = (hue / 360.0).toFloat() % 1.0f
            val s = 1.0f
            val l = 0.5f

            fun f(n: Float): Float {
                val k = (n + h * 12) % 12
                val a = s * Math.min(l, 1 - l)
                return (l - a * Math.max(Math.min(Math.min(k - 3, 9 - k), 1.0f), -1.0f)).toFloat()
            }

            val r = f(0f)
            val g = f(8f)
            val b = f(4f)

            return Color(r, g, b)
        }
    }
}
