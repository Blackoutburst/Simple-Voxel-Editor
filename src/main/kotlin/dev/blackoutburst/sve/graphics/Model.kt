package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.shader.Shader
import dev.blackoutburst.sve.shader.ShaderProgram
import dev.blackoutburst.sve.utils.stack
import org.lwjgl.opengl.GL30.*

class Model {

    var name = "unnamed"

    companion object {
        private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/model.vert")
        private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/model.frag")
        private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
    }

    val voxels = mutableListOf<Voxel>()

    private var vaoId = 0
    private var vboId = 0
    private var eboId = 0

    private var vertices: FloatArray? = null
    private var indices: IntArray? = null

    private var indexCount = 0

    init {
        vaoId = glGenVertexArrays()
        vboId = glGenBuffers()
        eboId = glGenBuffers()

        updateModel()
    }

    private fun generateVAO() {
        stack(128 * 1024) { stack ->
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
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0)

            // COLOR
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 32, 12)

            // NORMAL
            glEnableVertexAttribArray(2)
            glVertexAttribPointer(2, 1, GL_FLOAT, false, 32, 28)
        }

        indexCount = indices?.size ?: 0

        vertices = null
        indices = null
    }

    private fun calculateVertexArray() {
        val vertexArray = mutableListOf<Float>()
        voxels.forEach {
            // TOP
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 0f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 0f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 0f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 0f))

            // FRONT
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 1f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 1f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 1f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 1f))

            // BACK
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 2f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 2f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 2f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 2f))

            // LEFT
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 3f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 3f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 3f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 3f))

            // RIGHT
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 4f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 4f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + 0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 4f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 4f))

            // BOTTOM
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 5f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + -0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 5f))
            vertexArray.addAll(listOf(it.position.x + 0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 5f))
            vertexArray.addAll(listOf(it.position.x + -0.5f, it.position.y + -0.5f, it.position.z + 0.5f, it.color.r, it.color.g, it.color.b, it.color.a, 5f))
        }

        vertices = vertexArray.toFloatArray()
    }

    fun calculateIndexArray() {
        val indexArray = mutableListOf<Int>()
        var offset = 0
        voxels.forEach {
            indexArray.addAll(
                listOf(
                    // TOP
                    0 + offset, 2 + offset, 1 + offset,
                    0 + offset, 3 + offset, 2 + offset,

                    // FRONT
                    4 + offset, 6 + offset, 5 + offset,
                    4 + offset, 7 + offset, 6 + offset,

                    // BACK
                    9 + offset, 10 + offset, 8 + offset,
                    10 + offset, 11 + offset, 8 + offset,

                    // LEFT
                    12 + offset, 14 + offset, 13 + offset,
                    12 + offset, 15 + offset, 14 + offset,

                    // RIGHT
                    17 + offset, 18 + offset, 16 + offset,
                    18 + offset, 19 + offset, 16 + offset,

                    // BOTTOM
                    21 + offset, 22 + offset, 20 + offset,
                    22 + offset, 23 + offset, 20 + offset,
                )
            )
            offset += 24
        }

        indices = indexArray.toIntArray()
    }

    fun updateModel() {
        calculateVertexArray()
        calculateIndexArray()
        generateVAO()
    }

    fun addVoxel(voxel: Voxel) {
        voxels.add(voxel)
        updateModel()
    }

    fun removeVoxel(voxel: Voxel) {
        if (voxels.size == 1) return

        voxels.remove(voxel)
        updateModel()
    }

    fun render() {
        glBindVertexArray(vaoId)
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniform3f("viewPos", Camera.position)
        shaderProgram.setUniformMat4("view", Camera.view)
        shaderProgram.setUniformMat4("projection", Camera.projection)

        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }
}