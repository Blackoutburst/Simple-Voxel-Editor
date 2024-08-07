package dev.blackoutburst.sve.window.callbacks

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW.glfwSetWindowSize
import org.lwjgl.glfw.GLFWWindowSizeCallbackI
import org.lwjgl.opengl.GL11.glViewport

class WindowCallBack : GLFWWindowSizeCallbackI {
    override fun invoke(window: Long, width: Int, height: Int) {
        glfwSetWindowSize(window, width, height)
        glViewport(0, 0, width, height)
        Camera.projection2D = Matrix().ortho2D(0f, width.toFloat(), 0f, height.toFloat(), -1f, 1f)

    }
}