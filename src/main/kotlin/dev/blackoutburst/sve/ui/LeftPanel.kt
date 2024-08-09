package dev.blackoutburst.sve.ui

import dev.blackoutburst.sve.Main.model
import dev.blackoutburst.sve.files.FileExplorer
import dev.blackoutburst.sve.graphics.ColoredBox
import dev.blackoutburst.sve.graphics.HueBox
import dev.blackoutburst.sve.graphics.ColorPickerBox
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.io.SVEFiles
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.utils.main
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW.*

object LeftPanel {
    var clicked = false

    private val picker = ColorPickerBox(10f, 30f, 180f, 180f)
    private val hueSlider = HueBox(10f, 10f, 180f, 10f)

    private val importButton = Button(10f, Window.height - 40f, 180f, 30f, "Import")
    private val exportButton = Button(10f, Window.height - 80f, 180f, 30f, "Export")

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
        if (Keyboard.isKeyPressed(GLFW_KEY_P))
            visible = !visible

        clicked = (Mouse.isButtonDown(Mouse.LEFT_BUTTON) || Mouse.isButtonDown(Mouse.RIGHT_BUTTON)) && Mouse.position.x <= 200f && visible
        if (!visible) return

        importButton.y = Window.height - 40f
        importButton.onHover { importButton.backgroundColor = Color(0.15f) }
        importButton.onExit { importButton.backgroundColor = Color(0.1f) }
        importButton.onClick { FileExplorer.pickFile { if (it != null) main { model = SVEFiles.load(it) } } }

        exportButton.y = Window.height - 80f
        exportButton.onHover { exportButton.backgroundColor = Color(0.15f) }
        exportButton.onExit { exportButton.backgroundColor = Color(0.1f) }
        exportButton.onClick { FileExplorer.saveFile { if (it != null) main { SVEFiles.export(it, model!!) } } }

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
    }

    fun render() {
        if (!visible) return
        background.render()
        picker.render()
        hueSlider.render()
        selectedColorHistory.forEach {
            it.render()
        }

        importButton.render()
        exportButton.render()
    }
}