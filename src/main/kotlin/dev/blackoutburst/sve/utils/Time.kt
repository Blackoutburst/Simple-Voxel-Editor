package dev.blackoutburst.sve.utils

import org.lwjgl.glfw.GLFW

object Time {
    const val UPDATE = 1e9f / 20.0
    private var init = System.nanoTime()
    private var lastTime = System.nanoTime()
    private var deltaTime = 0.0

    fun updateDelta() {
        val time = System.nanoTime()
        deltaTime = ((time - lastTime) / 1e9f).toDouble()
        lastTime = time
    }

    val delta: Double
        get() = (deltaTime)

    val runtime: Double
        get() = (GLFW.glfwGetTime())
}