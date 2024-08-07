package dev.blackoutburst.sve.camera

import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.maths.Vector2f
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.utils.Time
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

object Camera {
    private var velocity = Vector3f()
    private var moving = false
    private var sprint = false
    private val runSpeed = 50f
    private val walkSpeed = 10f
    private var lastMousePosition = Mouse.position
    private val sensitivity = 0.1f

    var position = Vector3f()
    var rotation = Vector2f()

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
        rotate()
        move()

        view.setIdentity()
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
            .translate(Vector3f(-position.x, -position.y, -position.z))
    }

    private fun rotate() {
        val mousePosition = Mouse.position

        var xOffset = mousePosition.x - lastMousePosition.x
        var yOffset = mousePosition.y - lastMousePosition.y

        lastMousePosition = mousePosition.copy()

        xOffset *= sensitivity
        yOffset *= sensitivity

        rotation.x += xOffset
        rotation.y += yOffset

        if (rotation.y > 89.0f) rotation.y = 89.0f
        if (rotation.y < -89.0f) rotation.y = -89.0f
    }

    private fun move() {
        moving = false

        if ((Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))) {
            sprint = true
        }

        if ((Keyboard.isKeyDown(GLFW.GLFW_KEY_W))) {
            velocity.x -= sin(-rotation.x * Math.PI / 180).toFloat()
            velocity.z -= cos(-rotation.x * Math.PI / 180).toFloat()
            moving = true
        }

        if ((Keyboard.isKeyDown(GLFW.GLFW_KEY_S))) {
            velocity.x += sin(-rotation.x * Math.PI / 180).toFloat()
            velocity.z += cos(-rotation.x * Math.PI / 180).toFloat()
            moving = true
        }

        if ((Keyboard.isKeyDown(GLFW.GLFW_KEY_A))) {
            velocity.x += sin((-rotation.x - 90) * Math.PI / 180).toFloat()
            velocity.z += cos((-rotation.x - 90) * Math.PI / 180).toFloat()
            moving = true
        }

        if ((Keyboard.isKeyDown(GLFW.GLFW_KEY_D))) {
            velocity.x += sin((-rotation.x + 90) * Math.PI / 180).toFloat()
            velocity.z += cos((-rotation.x + 90) * Math.PI / 180).toFloat()
            moving = true
        }

        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            velocity.y += 1
            moving = true
        }

        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            velocity.y -= 1
            moving = true
        }

        val speed = if (sprint) runSpeed else walkSpeed


        if (moving) {
            val horizontalVelocity = Vector3f(velocity.x, 0f, velocity.z)

            velocity.x = horizontalVelocity.normalize().x
            velocity.z = horizontalVelocity.normalize().z
        } else {
            sprint = false
        }

        position.x += velocity.x * Time.delta.toFloat() * speed
        position.y += velocity.y * Time.delta.toFloat() * speed
        position.z += velocity.z * Time.delta.toFloat() * speed

        velocity.zero()
    }
}
