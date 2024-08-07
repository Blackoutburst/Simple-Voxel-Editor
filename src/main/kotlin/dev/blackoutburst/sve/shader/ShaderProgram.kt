package dev.blackoutburst.sve.shader

import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.maths.Vector2f
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.maths.Vector4f
import dev.blackoutburst.sve.utils.Color
import org.lwjgl.opengl.GL41.*

class ShaderProgram(vertex: Shader, fragment: Shader) {
    var id = -1

    init {
        id = glCreateProgram()
        glAttachShader(id, vertex.id)
        glAttachShader(id, fragment.id)
        glLinkProgram(id)
        glDetachShader(id, vertex.id)
        glDetachShader(id, fragment.id)
    }

    fun destroy() = glDeleteProgram(id)

    fun setUniform1i(varName: String, x: Int) = glProgramUniform1i(id, glGetUniformLocation(id, varName), x)

    fun setUniform1f(varName: String, x: Float) = glProgramUniform1f(id, glGetUniformLocation(id, varName), x)

    fun setUniform2f(varName: String, x: Float, y: Float) = glProgramUniform2f(id, glGetUniformLocation(id, varName), x, y)

    fun setUniform2f(varName: String, vec: Vector2f) = glProgramUniform2f(id, glGetUniformLocation(id, varName), vec.x, vec.y)

    fun setUniform3f(varName: String, x: Float, y: Float, z: Float) = glProgramUniform3f(id, glGetUniformLocation(id, varName), x, y, z)

    fun setUniform3f(varName: String, vec: Vector3f) = glProgramUniform3f(id, glGetUniformLocation(id, varName), vec.x, vec.y, vec.z)

    fun setUniform3f(varName: String, color: Color) = glProgramUniform3f(id, glGetUniformLocation(id, varName), color.r, color.g, color.b)

    fun setUniform4f(varName: String, x: Float, y: Float, z: Float, w: Float) = glProgramUniform4f(id, glGetUniformLocation(id, varName), x, y, z, w)

    fun setUniform4f(varName: String, vec: Vector4f) = glProgramUniform4f(id, glGetUniformLocation(id, varName), vec.x, vec.y, vec.z, vec.w)

    fun setUniform4f(varName: String, color: Color) = glProgramUniform4f(id, glGetUniformLocation(id, varName), color.r, color.g, color.b, color.a)

    fun setUniformMat4(varName: String, mat: Matrix) = glProgramUniformMatrix4fv(id, glGetUniformLocation(id, varName), false, mat.getValues())
}
