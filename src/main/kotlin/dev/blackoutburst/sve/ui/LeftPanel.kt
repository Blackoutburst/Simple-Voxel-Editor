package dev.blackoutburst.sve.ui

import dev.blackoutburst.sve.graphics.ColoredBox
import dev.blackoutburst.sve.graphics.HueBox
import dev.blackoutburst.sve.graphics.ColorPickerBox
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW

object LeftPanel {
    var clicked = false

    private val picker = ColorPickerBox(10f, 30f, 180f, 180f)
    private val hueSlider = HueBox(10f, 10f, 180f, 10f)

    private var selectedColorHistory: List<ColoredBox> = emptyList()

    init {
        val boxes = mutableListOf<ColoredBox>()
        for (y in 0 until 2) {
            for (x in 0 until 5) {
                boxes.add(ColoredBox(10f + 37f * x, 256f - 37f * y, 32f, 32f, Color.WHITE),)
            }
        }

        selectedColorHistory =  boxes.toList()
    }

    var selectedColor = Color.WHITE

    private val background = ColoredBox(0f, 0f, 200f, Window.height.toFloat(), Color(0.15f, 0.15f, 0.15f))
    private var visible = true

    private fun getColorFromHistory() {
        val mouseX = Mouse.position.x
        val mouseY = Window.height - Mouse.position.y

        selectedColorHistory.forEach { box ->
            if (mouseX >= box.x &&
                mouseX <= box.x + box.width &&
                mouseY >= box.y &&
                mouseY <= box.y + box.height &&
                Mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
                selectedColor = box.color
            }
        }

    }

    fun update() {
        clicked = (Mouse.isButtonDown(Mouse.LEFT_BUTTON) || Mouse.isButtonDown(Mouse.RIGHT_BUTTON)) && Mouse.position.x <= 200f && visible

        getColorFromHistory()

        background.height = Window.height.toFloat()

        if (Mouse.isButtonDown(Mouse.LEFT_BUTTON)) {
            picker.color = hueSlider.selectColor(picker.color)
            selectedColor = picker.selectColor(selectedColor)
        }

        if (Mouse.isButtonReleased(Mouse.LEFT_BUTTON) &&
            (selectedColorHistory[0].color.r != selectedColor.r ||
            selectedColorHistory[0].color.g != selectedColor.g ||
            selectedColorHistory[0].color.b != selectedColor.b ||
            selectedColorHistory[0].color.a != selectedColor.a)) {

            for (i in selectedColorHistory.size - 1 downTo 1) {
                selectedColorHistory[i].color = selectedColorHistory[i - 1].color
            }

            selectedColorHistory[0].color = selectedColor
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_P))
            visible = !visible
    }

    fun render() {
        if (!visible) return
        background.render()
        picker.render()
        hueSlider.render()
        selectedColorHistory.forEach {
            it.render()
        }
    }
}