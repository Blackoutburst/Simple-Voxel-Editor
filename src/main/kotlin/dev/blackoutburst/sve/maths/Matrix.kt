package dev.blackoutburst.sve.maths

import dev.blackoutburst.sve.window.Window
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix {
    var m00: Float = 0f
    var m01: Float = 0f
    var m02: Float = 0f
    var m03: Float = 0f
    var m10: Float = 0f
    var m11: Float = 0f
    var m12: Float = 0f
    var m13: Float = 0f
    var m20: Float = 0f
    var m21: Float = 0f
    var m22: Float = 0f
    var m23: Float = 0f
    var m30: Float = 0f
    var m31: Float = 0f
    var m32: Float = 0f
    var m33: Float = 0f

    constructor() {
        this.setIdentity()
    }

    constructor(matrix: Matrix) {
        this.m00 = matrix.m00
        this.m01 = matrix.m01
        this.m02 = matrix.m02
        this.m03 = matrix.m03
        this.m10 = matrix.m10
        this.m11 = matrix.m11
        this.m12 = matrix.m12
        this.m13 = matrix.m13
        this.m20 = matrix.m20
        this.m21 = matrix.m21
        this.m22 = matrix.m22
        this.m23 = matrix.m23
        this.m30 = matrix.m30
        this.m31 = matrix.m31
        this.m32 = matrix.m32
        this.m33 = matrix.m33
    }

    fun get(index: Int): Float {
        val elements = floatArrayOf(
            this.m00, this.m01, this.m02, this.m03,
            this.m10, this.m11, this.m12, this.m13,
            this.m20, this.m21, this.m22, this.m23,
            this.m30, this.m31, this.m32, this.m33
        )

        return elements[index]
    }

    fun mul(right: Matrix): Matrix {
        val src = Matrix()
        load(this, src)

        val m00 = this.m00 * right.m00 + this.m10 * right.m01 + this.m20 * right.m02 + this.m30 * right.m03
        val m01 = this.m01 * right.m00 + this.m11 * right.m01 + this.m21 * right.m02 + this.m31 * right.m03
        val m02 = this.m02 * right.m00 + this.m12 * right.m01 + this.m22 * right.m02 + this.m32 * right.m03
        val m03 = this.m03 * right.m00 + this.m13 * right.m01 + this.m23 * right.m02 + this.m33 * right.m03
        val m10 = this.m00 * right.m10 + this.m10 * right.m11 + this.m20 * right.m12 + this.m30 * right.m13
        val m11 = this.m01 * right.m10 + this.m11 * right.m11 + this.m21 * right.m12 + this.m31 * right.m13
        val m12 = this.m02 * right.m10 + this.m12 * right.m11 + this.m22 * right.m12 + this.m32 * right.m13
        val m13 = this.m03 * right.m10 + this.m13 * right.m11 + this.m23 * right.m12 + this.m33 * right.m13
        val m20 = this.m00 * right.m20 + this.m10 * right.m21 + this.m20 * right.m22 + this.m30 * right.m23
        val m21 = this.m01 * right.m20 + this.m11 * right.m21 + this.m21 * right.m22 + this.m31 * right.m23
        val m22 = this.m02 * right.m20 + this.m12 * right.m21 + this.m22 * right.m22 + this.m32 * right.m23
        val m23 = this.m03 * right.m20 + this.m13 * right.m21 + this.m23 * right.m22 + this.m33 * right.m23
        val m30 = this.m00 * right.m30 + this.m10 * right.m31 + this.m20 * right.m32 + this.m30 * right.m33
        val m31 = this.m01 * right.m30 + this.m11 * right.m31 + this.m21 * right.m32 + this.m31 * right.m33
        val m32 = this.m02 * right.m30 + this.m12 * right.m31 + this.m22 * right.m32 + this.m32 * right.m33
        val m33 = this.m03 * right.m30 + this.m13 * right.m31 + this.m23 * right.m32 + this.m33 * right.m33

        this.m00 = m00
        this.m01 = m01
        this.m02 = m02
        this.m03 = m03
        this.m10 = m10
        this.m11 = m11
        this.m12 = m12
        this.m13 = m13
        this.m20 = m20
        this.m21 = m21
        this.m22 = m22
        this.m23 = m23
        this.m30 = m30
        this.m31 = m31
        this.m32 = m32
        this.m33 = m33

        return this
    }


    fun lookAt(eye: Vector3f, center: Vector3f, up: Vector3f): Matrix {
        val f = (center - eye).normalize()
        val u = f.cross(up).normalize()
        val s = f.cross(u)

        this.m00 = s.x
        this.m01 = s.y
        this.m02 = s.z
        this.m10 = u.x
        this.m11 = u.y
        this.m12 = u.z
        this.m20 = -f.x
        this.m21 = -f.y
        this.m22 = -f.z
        this.m30 = -s.dot(eye)
        this.m31 = -u.dot(eye)
        this.m32 = f.dot(eye)

        return this
    }

    infix fun DoubleArray.dot(other: DoubleArray): Double {
        var out = 0.0
        for (i in 0 until size) out += this[i] * other[i]
        return out
    }

    fun ortho2D(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix {
        val x_orth = 2.0f / (right - left)
        val y_orth = 2.0f / (top - bottom)
        val z_orth = -2.0f / (far - near)

        val tx = -(right + left) / (right - left)
        val ty = -(top + bottom) / (top - bottom)
        val tz = -(far + near) / (far - near)

        this.m00 = x_orth
        this.m10 = 0f
        this.m20 = 0f
        this.m30 = tx
        this.m01 = 0f
        this.m11 = y_orth
        this.m21 = 0f
        this.m31 = ty
        this.m02 = 0f
        this.m12 = 0f
        this.m22 = z_orth
        this.m32 = tz
        this.m03 = 0f
        this.m13 = 0f
        this.m23 = 0f
        this.m33 = 1f

        return (this)
    }

    fun projectionMatrix(fov: Float, far: Float, near: Float): Matrix {
        val aspectRatio = Window.width.toFloat() / Window.height.toFloat()
        val y_scale = ((1f / tan(Math.toRadians((fov / 2f).toDouble()))) * aspectRatio).toFloat()
        val x_scale = y_scale / aspectRatio
        val frustum_length = far - near

        this.m00 = x_scale
        this.m11 = y_scale
        this.m22 = -((far + near) / frustum_length)
        this.m23 = -1f
        this.m32 = -((2 * near * far) / frustum_length)
        this.m33 = 0f
        return (this)
    }

    override fun toString(): String {
        val buf = StringBuilder()
        buf.append('[').append(m00).append(' ').append(m10).append(' ').append(m20).append(' ').append(m30).append(']')
            .append('\n')
        buf.append('[').append(m01).append(' ').append(m11).append(' ').append(m21).append(' ').append(m31).append(']')
            .append('\n')
        buf.append('[').append(m02).append(' ').append(m12).append(' ').append(m22).append(' ').append(m32).append(']')
            .append('\n')
        buf.append('[').append(m03).append(' ').append(m13).append(' ').append(m23).append(' ').append(m33).append(']')
            .append('\n')
        return buf.toString()
    }

    fun load(buf: FloatBuffer): Matrix {
        m00 = buf.get()
        m01 = buf.get()
        m02 = buf.get()
        m03 = buf.get()
        m10 = buf.get()
        m11 = buf.get()
        m12 = buf.get()
        m13 = buf.get()
        m20 = buf.get()
        m21 = buf.get()
        m22 = buf.get()
        m23 = buf.get()
        m30 = buf.get()
        m31 = buf.get()
        m32 = buf.get()
        m33 = buf.get()

        return this
    }

    fun store(buf: FloatBuffer): Matrix {
        buf.put(m00)
        buf.put(m01)
        buf.put(m02)
        buf.put(m03)
        buf.put(m10)
        buf.put(m11)
        buf.put(m12)
        buf.put(m13)
        buf.put(m20)
        buf.put(m21)
        buf.put(m22)
        buf.put(m23)
        buf.put(m30)
        buf.put(m31)
        buf.put(m32)
        buf.put(m33)
        return this
    }

    fun storeTranspose(buf: FloatBuffer): Matrix {
        buf.put(m00)
        buf.put(m10)
        buf.put(m20)
        buf.put(m30)
        buf.put(m01)
        buf.put(m11)
        buf.put(m21)
        buf.put(m31)
        buf.put(m02)
        buf.put(m12)
        buf.put(m22)
        buf.put(m32)
        buf.put(m03)
        buf.put(m13)
        buf.put(m23)
        buf.put(m33)
        return this
    }

    @JvmOverloads
    fun transpose(dest: Matrix? = this): Matrix {
        return transpose(this, dest)
    }

    fun determinant(): Float {
        var f = (
                m00
                        * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                        - (m13 * m22 * m31
                        ) - (m11 * m23 * m32
                        ) - (m12 * m21 * m33)))
        f -= (m01
                * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                - (m13 * m22 * m30
                ) - (m10 * m23 * m32
                ) - (m12 * m20 * m33)))
        f += (m02
                * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                - (m13 * m21 * m30
                ) - (m10 * m23 * m31
                ) - (m11 * m20 * m33)))
        f -= (m03
                * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                - (m12 * m21 * m30
                ) - (m10 * m22 * m31
                ) - (m11 * m20 * m32)))
        return f
    }

    @JvmOverloads
    fun negate(dest: Matrix? = this): Matrix {
        return negate(this, dest)
    }

    fun getValues(): FloatArray {
        return (floatArrayOf(
            this.m00, this.m01, this.m02, this.m03,
            this.m10, this.m11, this.m12, this.m13,
            this.m20, this.m21, this.m22, this.m23,
            this.m30, this.m31, this.m32, this.m33
        ))
    }

    fun setIdentity(): Matrix {
        this.m00 = 1.0f
        this.m01 = 0.0f
        this.m02 = 0.0f
        this.m03 = 0.0f
        this.m10 = 0.0f
        this.m11 = 1.0f
        this.m12 = 0.0f
        this.m13 = 0.0f
        this.m20 = 0.0f
        this.m21 = 0.0f
        this.m22 = 1.0f
        this.m23 = 0.0f
        this.m30 = 0.0f
        this.m31 = 0.0f
        this.m32 = 0.0f
        this.m33 = 1.0f

        return this
    }

    fun setZero(m: Matrix): Matrix {
        m.m00 = 0.0f
        m.m01 = 0.0f
        m.m02 = 0.0f
        m.m03 = 0.0f
        m.m10 = 0.0f
        m.m11 = 0.0f
        m.m12 = 0.0f
        m.m13 = 0.0f
        m.m20 = 0.0f
        m.m21 = 0.0f
        m.m22 = 0.0f
        m.m23 = 0.0f
        m.m30 = 0.0f
        m.m31 = 0.0f
        m.m32 = 0.0f
        m.m33 = 0.0f

        return m
    }

    @JvmOverloads
    fun load(src: Matrix, dest: Matrix? = this): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()
        dest.m00 = src.m00
        dest.m01 = src.m01
        dest.m02 = src.m02
        dest.m03 = src.m03
        dest.m10 = src.m10
        dest.m11 = src.m11
        dest.m12 = src.m12
        dest.m13 = src.m13
        dest.m20 = src.m20
        dest.m21 = src.m21
        dest.m22 = src.m22
        dest.m23 = src.m23
        dest.m30 = src.m30
        dest.m31 = src.m31
        dest.m32 = src.m32
        dest.m33 = src.m33

        return dest
    }

    fun add(left: Matrix, right: Matrix, dest: Matrix?): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()

        dest.m00 = left.m00 + right.m00
        dest.m01 = left.m01 + right.m01
        dest.m02 = left.m02 + right.m02
        dest.m03 = left.m03 + right.m03
        dest.m10 = left.m10 + right.m10
        dest.m11 = left.m11 + right.m11
        dest.m12 = left.m12 + right.m12
        dest.m13 = left.m13 + right.m13
        dest.m20 = left.m20 + right.m20
        dest.m21 = left.m21 + right.m21
        dest.m22 = left.m22 + right.m22
        dest.m23 = left.m23 + right.m23
        dest.m30 = left.m30 + right.m30
        dest.m31 = left.m31 + right.m31
        dest.m32 = left.m32 + right.m32
        dest.m33 = left.m33 + right.m33

        return dest
    }

    fun sub(left: Matrix, right: Matrix, dest: Matrix?): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()

        dest.m00 = left.m00 - right.m00
        dest.m01 = left.m01 - right.m01
        dest.m02 = left.m02 - right.m02
        dest.m03 = left.m03 - right.m03
        dest.m10 = left.m10 - right.m10
        dest.m11 = left.m11 - right.m11
        dest.m12 = left.m12 - right.m12
        dest.m13 = left.m13 - right.m13
        dest.m20 = left.m20 - right.m20
        dest.m21 = left.m21 - right.m21
        dest.m22 = left.m22 - right.m22
        dest.m23 = left.m23 - right.m23
        dest.m30 = left.m30 - right.m30
        dest.m31 = left.m31 - right.m31
        dest.m32 = left.m32 - right.m32
        dest.m33 = left.m33 - right.m33

        return dest
    }

    fun mul(left: Matrix, right: Matrix, dest: Matrix?): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()

        val m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03
        val m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03
        val m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03
        val m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03
        val m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13
        val m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13
        val m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13
        val m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13
        val m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23
        val m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23
        val m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23
        val m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23
        val m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33
        val m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33
        val m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33
        val m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33

        dest.m00 = m00
        dest.m01 = m01
        dest.m02 = m02
        dest.m03 = m03
        dest.m10 = m10
        dest.m11 = m11
        dest.m12 = m12
        dest.m13 = m13
        dest.m20 = m20
        dest.m21 = m21
        dest.m22 = m22
        dest.m23 = m23
        dest.m30 = m30
        dest.m31 = m31
        dest.m32 = m32
        dest.m33 = m33

        return dest
    }

    fun scale(vec: Vector2f): Matrix {
        this.m00 = this.m00 * vec.x
        this.m01 = this.m01 * vec.x
        this.m02 = this.m02 * vec.x
        this.m03 = this.m03 * vec.x
        this.m10 = this.m10 * vec.y
        this.m11 = this.m11 * vec.y
        this.m12 = this.m12 * vec.y
        this.m13 = this.m13 * vec.y
        this.m20 = this.m20 * 1
        this.m21 = this.m21 * 1
        this.m22 = this.m22 * 1
        this.m23 = this.m23 * 1

        return this
    }

    fun scale(x: Float, y: Float): Matrix {
        this.m00 = this.m00 * x
        this.m01 = this.m01 * x
        this.m02 = this.m02 * x
        this.m03 = this.m03 * x
        this.m10 = this.m10 * y
        this.m11 = this.m11 * y
        this.m12 = this.m12 * y
        this.m13 = this.m13 * y
        this.m20 = this.m20 * 1
        this.m21 = this.m21 * 1
        this.m22 = this.m22 * 1
        this.m23 = this.m23 * 1

        return this
    }

    fun scale(vec: Vector3f): Matrix {
        this.m00 = this.m00 * vec.x
        this.m01 = this.m01 * vec.x
        this.m02 = this.m02 * vec.x
        this.m03 = this.m03 * vec.x
        this.m10 = this.m10 * vec.y
        this.m11 = this.m11 * vec.y
        this.m12 = this.m12 * vec.y
        this.m13 = this.m13 * vec.y
        this.m20 = this.m20 * vec.z
        this.m21 = this.m21 * vec.z
        this.m22 = this.m22 * vec.z
        this.m23 = this.m23 * vec.z

        return this
    }

    fun rotate(angle: Float, axis: Vector3f): Matrix {
        val src = Matrix()
        load(this, src)

        val c = cos(angle.toDouble()).toFloat()
        val s = sin(angle.toDouble()).toFloat()
        val oneminusc = 1.0f - c
        val xy = axis.x * axis.y
        val yz = axis.y * axis.z
        val xz = axis.x * axis.z
        val xs = axis.x * s
        val ys = axis.y * s
        val zs = axis.z * s

        val f00 = axis.x * axis.x * oneminusc + c
        val f01 = xy * oneminusc + zs
        val f02 = xz * oneminusc - ys
        // n[3] not used
        val f10 = xy * oneminusc - zs
        val f11 = axis.y * axis.y * oneminusc + c
        val f12 = yz * oneminusc + xs
        // n[7] not used
        val f20 = xz * oneminusc + ys
        val f21 = yz * oneminusc - xs
        val f22 = axis.z * axis.z * oneminusc + c

        val t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02
        val t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02
        val t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02
        val t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02
        val t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12
        val t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12
        val t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12
        val t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12
        this.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22
        this.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22
        this.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22
        this.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22
        this.m00 = t00
        this.m01 = t01
        this.m02 = t02
        this.m03 = t03
        this.m10 = t10
        this.m11 = t11
        this.m12 = t12
        this.m13 = t13

        return (this)
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float): Matrix {
        val src = Matrix()
        load(this, src)

        val c = cos(angle.toDouble()).toFloat()
        val s = sin(angle.toDouble()).toFloat()
        val oneminusc = 1.0f - c
        val xy = x * y
        val yz = y * z
        val xz = x * z
        val xs = x * s
        val ys = y * s
        val zs = z * s

        val f00 = x * x * oneminusc + c
        val f01 = xy * oneminusc + zs
        val f02 = xz * oneminusc - ys
        // n[3] not used
        val f10 = xy * oneminusc - zs
        val f11 = y * y * oneminusc + c
        val f12 = yz * oneminusc + xs
        // n[7] not used
        val f20 = xz * oneminusc + ys
        val f21 = yz * oneminusc - xs
        val f22 = z * z * oneminusc + c

        val t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02
        val t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02
        val t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02
        val t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02
        val t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12
        val t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12
        val t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12
        val t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12
        this.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22
        this.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22
        this.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22
        this.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22
        this.m00 = t00
        this.m01 = t01
        this.m02 = t02
        this.m03 = t03
        this.m10 = t10
        this.m11 = t11
        this.m12 = t12
        this.m13 = t13

        return (this)
    }

    fun rotate(angle: Float) {
        val src = Matrix()
        load(this, src)

        val c = cos(angle.toDouble()).toFloat()
        val s = sin(angle.toDouble()).toFloat()
        val oneminusc = 1.0f - c
        val xy = 0f
        val yz = 0f
        val xz = 0f
        val xs = 0f
        val ys = 0f
        val zs = 1 * s

        val f00 = 0 * oneminusc + c
        val f01 = xy * oneminusc + zs
        val f02 = xz * oneminusc - ys
        // n[3] not used
        val f10 = xy * oneminusc - zs
        val f11 = 0 * oneminusc + c
        val f12 = yz * oneminusc + xs
        // n[7] not used
        val f20 = xz * oneminusc + ys
        val f21 = yz * oneminusc - xs
        val f22 = 1 * oneminusc + c

        val t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02
        val t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02
        val t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02
        val t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02
        val t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12
        val t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12
        val t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12
        val t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12
        this.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22
        this.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22
        this.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22
        this.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22
        this.m00 = t00
        this.m01 = t01
        this.m02 = t02
        this.m03 = t03
        this.m10 = t10
        this.m11 = t11
        this.m12 = t12
        this.m13 = t13
    }


    fun translate(vec: Vector2f): Matrix {
        val src = Matrix()
        load(this, src)

        this.m30 += src.m00 * vec.x + src.m10 * vec.y
        this.m31 += src.m01 * vec.x + src.m11 * vec.y
        this.m32 += src.m02 * vec.x + src.m12 * vec.y
        this.m33 += src.m03 * vec.x + src.m13 * vec.y

        return this
    }

    fun translate(x: Float, y: Float): Matrix {
        val src = Matrix()
        load(this, src)

        this.m30 += src.m00 * x + src.m10 * y
        this.m31 += src.m01 * x + src.m11 * y
        this.m32 += src.m02 * x + src.m12 * y
        this.m33 += src.m03 * x + src.m13 * y

        return this
    }

    fun translate(vec: Vector3f): Matrix {
        val src = Matrix()
        load(this, src)

        this.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z
        this.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z
        this.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z
        this.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z

        return (this)
    }

    fun translate(x: Float, y: Float, z: Float): Matrix {
        val src = Matrix()
        load(this, src)

        this.m30 += src.m00 * x + src.m10 * y + src.m20 * z
        this.m31 += src.m01 * x + src.m11 * y + src.m21 * z
        this.m32 += src.m02 * x + src.m12 * y + src.m22 * z
        this.m33 += src.m03 * x + src.m13 * y + src.m23 * z

        return (this)
    }

    fun translate(vec: Vector3i): Matrix {
        val src = Matrix()
        load(this, src)

        this.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z
        this.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z
        this.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z
        this.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z

        return (this)
    }

    fun transpose(src: Matrix, dest: Matrix?): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()
        val m00 = src.m00
        val m01 = src.m10
        val m02 = src.m20
        val m03 = src.m30
        val m10 = src.m01
        val m11 = src.m11
        val m12 = src.m21
        val m13 = src.m31
        val m20 = src.m02
        val m21 = src.m12
        val m22 = src.m22
        val m23 = src.m32
        val m30 = src.m03
        val m31 = src.m13
        val m32 = src.m23
        val m33 = src.m33

        dest.m00 = m00
        dest.m01 = m01
        dest.m02 = m02
        dest.m03 = m03
        dest.m10 = m10
        dest.m11 = m11
        dest.m12 = m12
        dest.m13 = m13
        dest.m20 = m20
        dest.m21 = m21
        dest.m22 = m22
        dest.m23 = m23
        dest.m30 = m30
        dest.m31 = m31
        dest.m32 = m32
        dest.m33 = m33

        return dest
    }

    private fun determinant3x3(
        t00: Float, t01: Float, t02: Float,
        t10: Float, t11: Float, t12: Float,
        t20: Float, t21: Float, t22: Float
    ): Float {
        return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20)
    }

    @JvmOverloads
    fun invert(src: Matrix = this, dest: Matrix? = this): Matrix? {
        var dest = dest
        val determinant = src.determinant()

        if (determinant != 0f) {
            /*
         * m00 m01 m02 m03
         * m10 m11 m12 m13
         * m20 m21 m22 m23
         * m30 m31 m32 m33
         */
            if (dest == null) dest = Matrix()
            val determinant_inv = 1f / determinant

            // first row
            val t00 =
                determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33)
            val t01 =
                -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33)
            val t02 =
                determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33)
            val t03 =
                -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32)
            // second row
            val t10 =
                -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33)
            val t11 =
                determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33)
            val t12 =
                -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33)
            val t13 =
                determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32)
            // third row
            val t20 =
                determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33)
            val t21 =
                -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33)
            val t22 =
                determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33)
            val t23 =
                -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32)
            // fourth row
            val t30 =
                -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23)
            val t31 =
                determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23)
            val t32 =
                -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23)
            val t33 =
                determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22)

            // transpose and divide by the determinant
            dest.m00 = t00 * determinant_inv
            dest.m11 = t11 * determinant_inv
            dest.m22 = t22 * determinant_inv
            dest.m33 = t33 * determinant_inv
            dest.m01 = t10 * determinant_inv
            dest.m10 = t01 * determinant_inv
            dest.m20 = t02 * determinant_inv
            dest.m02 = t20 * determinant_inv
            dest.m12 = t21 * determinant_inv
            dest.m21 = t12 * determinant_inv
            dest.m03 = t30 * determinant_inv
            dest.m30 = t03 * determinant_inv
            dest.m13 = t31 * determinant_inv
            dest.m31 = t13 * determinant_inv
            dest.m32 = t23 * determinant_inv
            dest.m23 = t32 * determinant_inv
            return dest
        } else return null
    }

    fun negate(src: Matrix, dest: Matrix?): Matrix {
        var dest = dest
        if (dest == null) dest = Matrix()

        dest.m00 = -src.m00
        dest.m01 = -src.m01
        dest.m02 = -src.m02
        dest.m03 = -src.m03
        dest.m10 = -src.m10
        dest.m11 = -src.m11
        dest.m12 = -src.m12
        dest.m13 = -src.m13
        dest.m20 = -src.m20
        dest.m21 = -src.m21
        dest.m22 = -src.m22
        dest.m23 = -src.m23
        dest.m30 = -src.m30
        dest.m31 = -src.m31
        dest.m32 = -src.m32
        dest.m33 = -src.m33

        return dest
    }

}
