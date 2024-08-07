package dev.blackoutburst.sve.camera

import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.maths.Vector2f
import dev.blackoutburst.sve.maths.Vector3f
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
        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_R)) {
            position = Vector3f(0f, 0f, 5f)
            positionOffset = Vector3f(0f, 0f, 0f)
            rotation = Vector2f(45f, 30f)
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
            .translate(Vector3f(0f, 0f, -position.z))
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
            .translate(Vector3f(-position.x + positionOffset.x, -position.y + positionOffset.y, positionOffset.z))

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
