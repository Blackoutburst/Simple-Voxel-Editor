package dev.blackoutburst.sve.ui

import dev.blackoutburst.sve.graphics.ColoredBox
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW

object LeftPanel {
    var clicked = false

    private val background = ColoredBox(0f, 0f, 200f, Window.height.toFloat(), Color(0.15f, 0.15f, 0.15f))
    private var visible = true

    fun update() {
        clicked = (Mouse.isButtonDown(Mouse.LEFT_BUTTON) || Mouse.isButtonDown(Mouse.RIGHT_BUTTON)) && Mouse.position.x <= 200f && visible

        background.height = Window.height.toFloat()

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_P))
            visible = !visible
    }

    fun render() {
        if (!visible) return
        background.render()
    }
}