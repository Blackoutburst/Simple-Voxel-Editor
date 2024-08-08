package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.shader.Shader
import dev.blackoutburst.sve.shader.ShaderProgram
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.utils.Color.Companion.hueToRGB
import dev.blackoutburst.sve.utils.stack
import dev.blackoutburst.sve.window.Window
import org.lwjgl.opengl.GL30.*

class HueBox(var x: Float, var y: Float, var width: Float, var height: Float) {
    private val vertices = floatArrayOf(
        0f, 0f, 0f, 0f,
        1f, 1f, 1f, 1f,
        0f, 1f, 0f, 1f,
        1f, 0f, 1f, 0f,
    )

    private val indices = intArrayOf(
        0, 1, 2,
        0, 3, 1,
    )

    private var vaoID = 0

    private var model = Matrix()

    private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/colorPicker.vert")
    private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/hue.frag")
    private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)

    init {
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
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8)
            glBindVertexArray(0)
        }
    }

    fun selectColor(currentColor: Color): Color {
        if (Mouse.position.x >= x &&
            Mouse.position.x <= x + width &&
            Window.height - Mouse.position.y >= y &&
            Window.height - Mouse.position.y <= y + height) {

            val hue = (Mouse.position.x - x) * (360 / width)
            return hueToRGB(hue)
        }
        return currentColor
    }

    fun render() {
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniformMat4("model", model.setIdentity().translate(x, y).scale(width, height))
        shaderProgram.setUniformMat4("projection", Camera.projection2D)

        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)
    }
}