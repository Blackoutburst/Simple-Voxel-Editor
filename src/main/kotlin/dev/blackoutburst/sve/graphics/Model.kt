package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.shader.Shader
import dev.blackoutburst.sve.shader.ShaderProgram
import dev.blackoutburst.sve.utils.stack
import org.lwjgl.opengl.GL30.*

class Model {

    companion object {
        private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/model.vert")
        private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/model.frag")
        private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
    }

    var vaoId = 0
    var vboId = 0
    var eboId = 0

    var vertices = intArrayOf()
    var indices = intArrayOf()

    init {
        vaoId = glGenVertexArrays()
        vboId = glGenBuffers()
        eboId = glGenBuffers()

        generateVAO()
    }

    private fun generateVAO() {
        stack(128 * 1024) { stack ->
            glBindVertexArray(vaoId)

            // VBO
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            val vertexBuffer = stack.mallocInt(vertices.size)
            vertexBuffer.put(vertices).flip()
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

            // EBO
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
            val indexBuffer = stack.mallocInt(indices.size)
            indexBuffer.put(indices).flip()
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

            // ATTRIB
            // POSITION
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0)

            // COLOR
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 32, 12)

            // NORMAL
            glEnableVertexAttribArray(2)
            glVertexAttribPointer(2, 1, GL_FLOAT, false, 32, 28)
        }
    }

    fun update() {

    }

    fun render() {
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniform3f("viewPos", Camera.position)
        shaderProgram.setUniformMat4("model", Matrix())
        shaderProgram.setUniformMat4("view", Camera.view)
        shaderProgram.setUniformMat4("projection", Matrix().projectionMatrix(90f, 1000f, 0.1f))

        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)
    }
}