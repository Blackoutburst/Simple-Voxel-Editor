package dev.blackoutburst.sve.ui

import dev.blackoutburst.sve.graphics.ColoredBox
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.graphics.Text
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.window.Window

class Button(
    x: Float,
    y: Float,
    var width: Float,
    var height: Float,
    var textDisplayed: String,
){
    var fontSize: Float = 16f
    var outlineSize: Float = 1f

    var backgroundColor: Color = Color(0.1f)
        set(value) {
            field = value
            background.color = value
        }

    var outlineColor: Color = Color(0.2f)
        set(value) {
            field = value
            outline.color = value
        }

    var x: Float = x
    set(value) {
        field = value
        outline.x = field - (1 * outlineSize)
        background.x = field
        text.x = x + width / 2 - text.width / 2
    }

    var y: Float = y
    set(value) {
        field = value
        outline.y = field - (1 * outlineSize)
        background.y = field
        text.y = y + height / 2 - text.height / 2
    }

    val outline = ColoredBox(x - (1 * outlineSize), y - (1  * outlineSize), width + (2 * outlineSize), height + ( 2 * outlineSize), outlineColor)
    val background = ColoredBox(x, y, width, height, backgroundColor)
    val text = Text(x + width / 2, y + height / 2, fontSize, textDisplayed)

    private var hover = false

    init {
        text.x -= text.width / 2
        text.y -= text.height / 2
    }

    fun onHover(unit: () -> Unit) {
        val mouseX = Mouse.position.x
        val mouseY = Window.height - Mouse.position.y

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            hover = true
            unit()
        }
    }

    fun onExit(unit: () -> Unit) {
        val mouseX = Mouse.position.x
        val mouseY = Window.height - Mouse.position.y

        if (!(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) && hover) {
            hover = false
            unit()
        }
    }

    fun onClick(unit: () -> Unit) {
        if (hover && Mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
            unit()
        }
    }

    fun render() {
        outline.render()
        background.render()
        text.render()
    }
}