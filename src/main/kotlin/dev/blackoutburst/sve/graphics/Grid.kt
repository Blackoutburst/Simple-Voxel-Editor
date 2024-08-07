package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.shader.Shader
import dev.blackoutburst.sve.shader.ShaderProgram
import dev.blackoutburst.sve.utils.stack
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL30.*

object Grid {

    private val size = 100f

    private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/grid.vert")
    private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/grid.frag")
    private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)

    private val vaoId = glGenVertexArrays()
    private val vboId = glGenBuffers()
    private val eboId = glGenBuffers()

    private var vertices: FloatArray? = null
    private var indices: IntArray? = null

    private var indexCount = 0

    fun generateGrid() {
        val vertexArray = mutableListOf<Float>()
        val indexArray = mutableListOf<Int>()

        for (x in -size.toInt() until size.toInt()) {
            vertexArray.addAll(listOf(x.toFloat(), 0f, -size, 0.2f, 0.2f, 0.2f, 1f))
            indexArray.add(vertexArray.size / 7 - 1)
            vertexArray.addAll(listOf(x.toFloat(), 0f, size, 0.2f, 0.2f, 0.2f, 1f))
            indexArray.add(vertexArray.size / 7 - 1)
        }
        for (z in -size.toInt() until size.toInt()) {
            vertexArray.addAll(listOf(-size, 0f, z.toFloat(), 0.2f, 0.2f, 0.2f, 1f))
            indexArray.add(vertexArray.size / 7 - 1)
            vertexArray.addAll(listOf(size, 0f, z.toFloat(), 0.2f, 0.2f, 0.2f, 1f))
            indexArray.add(vertexArray.size / 7 - 1)
        }

        vertices = vertexArray.toFloatArray()
        indices = indexArray.toIntArray()
    }

    init {
        generateGrid()

        stack(256 * 1024) { stack ->
            glBindVertexArray(vaoId)

            // VBO
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            val vertexBuffer = stack.mallocFloat(vertices?.size ?: 0)
            vertexBuffer.put(vertices).flip()
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

            // EBO
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
            val indexBuffer = stack.mallocInt(indices?.size ?: 0)
            indexBuffer.put(indices).flip()
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

            // ATTRIB
            // POSITION
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 28, 0)

            // COLOR
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 28, 12)
        }

        indexCount = indices?.size ?: 0

        vertices = null
        indices = null
    }

    fun render() {
        glBindVertexArray(vaoId)
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniformMat4("view", Camera.view)
        shaderProgram.setUniformMat4("projection", Camera.projection)

        glDrawElements(GL_LINES, indexCount, GL_UNSIGNED_INT, 0)
    }
    
}