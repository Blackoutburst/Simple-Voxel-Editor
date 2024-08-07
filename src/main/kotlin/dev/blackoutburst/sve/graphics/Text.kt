package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.shader.Shader
import dev.blackoutburst.sve.shader.ShaderProgram
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.utils.stack
import dev.blackoutburst.sve.utils.toAscii
import org.lwjgl.opengl.GL30.*
import kotlin.code
import kotlin.collections.addAll
import kotlin.collections.forEach
import kotlin.collections.toFloatArray
import kotlin.collections.toIntArray
import kotlin.collections.toTypedArray
import kotlin.let
import kotlin.run
import kotlin.text.replace
import kotlin.text.toCharArray
import kotlin.to

class Text(var x: Float, var y: Float, var size: Float = 16f, var text: String, processColor: Boolean = true) {

    private val colorCharMap = mapOf(
        "&0" to 200,
        "&1" to 201,
        "&2" to 202,
        "&3" to 203,
        "&4" to 204,
        "&5" to 205,
        "&6" to 206,
        "&7" to 207,
        "&8" to 208,
        "&9" to 209,
        "&a" to 210,
        "&b" to 211,
        "&c" to 212,
        "&d" to 213,
        "&e" to 214,
        "&f" to 215,
    )

    private val colorMap = mapOf(
        200 to Color(0f),
        201 to Color(0f, 0f, 0.5f),
        202 to Color(0f, 0.5f, 0f),
        203 to Color(0f, 0.5f, 0.5f),
        204 to Color(0.5f, 0f, 0f),
        205 to Color(0.5f, 0f, 0.5f),
        206 to Color(0.8f, 0.5f, 0f),
        207 to Color(0.5f),
        208 to Color(0.25f),
        209 to Color(0f, 0f, 1f),
        210 to Color(0f, 1f, 0f),
        211 to Color(0f, 1f, 1f),
        212 to Color(1f, 0f, 0f),
        213 to Color(1f, 0f, 1f),
        214 to Color(1f, 1f, 0f),
        215 to Color(1f),
    )

    private fun getVertices(pos: Float, x: Float, y: Float, color: Color) = floatArrayOf(
        0f + pos, 0f, (0f + x) / 16f, (0f + 15f - y) / 16f, color.r, color.g, color.b,
        1f + pos, 1f, (1f + x) / 16f, (1f + 15f - y) / 16f, color.r, color.g, color.b,
        0f + pos, 1f, (0f + x) / 16f, (1f + 15f - y) / 16f, color.r, color.g, color.b,
        1f + pos, 0f, (1f + x) / 16f, (0f + 15f - y) / 16f, color.r, color.g, color.b,
    )

    private fun getIndices(offset: Int) = intArrayOf(
        0 + offset, 1 + offset, 2 + offset,
        0 + offset, 3 + offset, 1 + offset,
    )

    private var indexCount = 0

    private var vaoID = 0

    private var model = Matrix()

    private val texture = Texture("./ascii.png")
    private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/text.vert")
    private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/text.frag")
    private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)

    init {
        var ascii = text.toAscii()
        if (processColor)
            colorCharMap.forEach { (k, v) -> ascii = ascii.replace(k, Char(v).toString()) }
        text = ascii

        val vertex = mutableListOf<Float>()
        val index = mutableListOf<Int>()

        var i = 0
        var color = Color.WHITE
        for (c in ascii.toCharArray()) {
            colorMap[c.code]?.let {
                color = it
            } ?: run {
                val x = c.code % 16
                val y = c.code / 16

                vertex.addAll(getVertices(i / 8f, x.toFloat(), y.toFloat(), color).toTypedArray())
                index.addAll(getIndices(i).toTypedArray())
                i += 4
            }
        }

        val vertices = vertex.toFloatArray()
        val indices = index.toIntArray()

        vaoID = glGenVertexArrays()
        val vboID = glGenBuffers()
        val eboID = glGenBuffers()

        stack { stack ->
            glBindVertexArray(vaoID)

            glBindBuffer(GL_ARRAY_BUFFER, vboID)
            val vertexBuffer = stack.mallocFloat(vertices.size)
            vertexBuffer.put(vertices).flip()
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
            val indexBuffer = stack.mallocInt(indices.size)
            indexBuffer.put(indices).flip()
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 28, 0)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 28, 8)
            glEnableVertexAttribArray(2)
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 28, 16)
            glBindVertexArray(0)
        }

        indexCount = indices.size
    }

    fun render() {
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniform1i("diffuseMap", 0)
        shaderProgram.setUniformMat4("model", model.translate(x, y).scale(size, size))
        shaderProgram.setUniformMat4("projection", Camera.projection2D)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture.id)

        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }
}