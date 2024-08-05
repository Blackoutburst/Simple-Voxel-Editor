package dev.blackoutburst.sve.window.callbacks

import org.lwjgl.glfw.GLFW.glfwSetWindowSize
import org.lwjgl.glfw.GLFWWindowSizeCallbackI
import org.lwjgl.opengl.GL11.glViewport

class WindowCallBack : GLFWWindowSizeCallbackI {
    override fun invoke(window: Long, width: Int, height: Int) {
        glfwSetWindowSize(window, width, height)
        glViewport(0, 0, width, height)
    }
}