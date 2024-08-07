package dev.blackoutburst.sve.camera

import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.maths.Vector2f
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.ui.LeftPanel
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

object Camera {
    private var lastMousePosition = Mouse.position
    private val sensitivity = 0.2f

    var position = Vector3f(0f, 0f, 5f)

    var positionOffset = Vector3f(0f, 0f, 0f)

    var rotation = Vector2f(45f, 30f)

    var view = Matrix().translate(position)
    var projection = Matrix().projectionMatrix(90f, 1000f, 0.1f)
    var projection2D = Matrix().ortho2D(0f, Window.width.toFloat(), 0f, Window.height.toFloat(), -1f, 1f)

    val direction: Vector3f
        get() {
            val radianYaw = Math.toRadians(rotation.x.toDouble() - 90).toFloat()
            val radianPitch = Math.toRadians(-rotation.y.toDouble()).toFloat()

            val x = cos(radianPitch) * cos(radianYaw)
            val y = sin(radianPitch)
            val z = cos(radianPitch) * sin(radianYaw)

            return Vector3f(x, y, z).normalize()
        }

    fun update() {
        if (LeftPanel.clicked) return

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_R)) {
            position.set(0f, 0f, 5f)
            positionOffset.set(0f, 0f, 0f)
            rotation.set(45f, 30f)
        }

        val mousePosition = Mouse.position

        var xOffset = mousePosition.x - lastMousePosition.x
        var yOffset = mousePosition.y - lastMousePosition.y
        xOffset *= sensitivity
        yOffset *= sensitivity

        lastMousePosition = mousePosition.copy()

        rotate(xOffset, yOffset)
        move(xOffset, yOffset)

        view.setIdentity()
            .translate(0f, 0f, -position.z)
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), 1f, 0f, 0f)
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), 0f, 1f, 0f)
            .translate(-position.x + positionOffset.x, -position.y + positionOffset.y, positionOffset.z)

    }

    private fun rotate(xOffset: Float, yOffset: Float) {
        if (!Mouse.isButtonDown(Mouse.LEFT_BUTTON)) return

        rotation.x += xOffset
        rotation.y += yOffset

        if (rotation.y > 89.0f) rotation.y = 89.0f
        if (rotation.y < -89.0f) rotation.y = -89.0f
    }

    private fun move(xOffset: Float, yOffset: Float) {
        position.z -= Mouse.scroll / 2f

        if (!Mouse.isButtonDown(Mouse.RIGHT_BUTTON)) return

        positionOffset.x += cos(-rotation.x * Math.PI / 180).toFloat() * xOffset / 50f
        positionOffset.z -= sin(-rotation.x * Math.PI / 180).toFloat() * xOffset / 50f

        positionOffset.y -= yOffset / 50f

    }
}
