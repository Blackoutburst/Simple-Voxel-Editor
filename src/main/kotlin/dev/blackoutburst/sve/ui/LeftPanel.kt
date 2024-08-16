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
import dev.blackoutburst.sve.graphics.Text
import dev.blackoutburst.sve.maths.Vector3f
import org.lwjgl.glfw.GLFW.*

object LeftPanel {
    var clicked = false

    var xMirror = false
    var yMirror = false
    var zMirror = false

    private val picker = ColorPickerBox(10f, 30f, 180f, 180f)
    private val hueSlider = HueBox(10f, 10f, 180f, 10f)

    private val importButton = Button(10f, Window.height - 40f, 180f, 30f, "Import")
    private val exportButton = Button(10f, Window.height - 80f, 180f, 30f, "Export")

    private val shiftText = Text(10f, Window.height - 120f, 16f, "Shift model position")

    private val shiftXP = Button(10f, Window.height - 160f, 50f, 30f, "X+")
    private val shiftXN = Button(70f, Window.height - 160f, 50f, 30f, "X-")

    private val shiftYP = Button(10f, Window.height - 200f, 50f, 30f, "Y+")
    private val shiftYN = Button(70f, Window.height - 200f, 50f, 30f, "Y-")

    private val shiftZP = Button(10f, Window.height - 240f, 50f, 30f, "Z+")
    private val shiftZN = Button(70f, Window.height - 240f, 50f, 30f, "Z-")

    private val mirrorText = Text(10f, Window.height - 280f, 16f, "Mirror")
    private val mirrorX = Button(10f, Window.height - 320f, 40f, 30f, "X")
    private val mirrorY = Button(60f, Window.height - 320f, 40f, 30f, "Y")
    private val mirrorZ = Button(110f, Window.height - 320f, 40f, 30f, "Z")

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

        shiftText.y = Window.height - 120f

        shiftXP.y = Window.height - 160f
        shiftXP.onHover { shiftXP.backgroundColor = Color(0.15f) }
        shiftXP.onExit { shiftXP.backgroundColor = Color(0.1f) }
        shiftXP.onClick { model!!.shiftPosition(Vector3f(1f, 0f, 0f)) }

        shiftXN.y = Window.height - 160f
        shiftXN.onHover { shiftXN.backgroundColor = Color(0.15f) }
        shiftXN.onExit { shiftXN.backgroundColor = Color(0.1f) }
        shiftXN.onClick { model!!.shiftPosition(Vector3f(-1f, 0f, 0f)) }

        shiftYP.y = Window.height - 200f
        shiftYP.onHover { shiftYP.backgroundColor = Color(0.15f) }
        shiftYP.onExit { shiftYP.backgroundColor = Color(0.1f) }
        shiftYP.onClick { model!!.shiftPosition(Vector3f(0f, 1f, 0f)) }

        shiftYN.y = Window.height - 200f
        shiftYN.onHover { shiftYN.backgroundColor = Color(0.15f) }
        shiftYN.onExit { shiftYN.backgroundColor = Color(0.1f) }
        shiftYN.onClick { model!!.shiftPosition(Vector3f(0f, -1f, 0f)) }

        shiftZP.y = Window.height - 240f
        shiftZP.onHover { shiftZP.backgroundColor = Color(0.15f) }
        shiftZP.onExit { shiftZP.backgroundColor = Color(0.1f) }
        shiftZP.onClick { model!!.shiftPosition(Vector3f(0f, 0f, 1f)) }

        shiftZN.y = Window.height - 240f
        shiftZN.onHover { shiftZN.backgroundColor = Color(0.15f) }
        shiftZN.onExit { shiftZN.backgroundColor = Color(0.1f) }
        shiftZN.onClick { model!!.shiftPosition(Vector3f(0f, 0f, -1f)) }

        mirrorText.y = Window.height - 280f

        mirrorX.y = Window.height - 320f
        mirrorX.outlineColor = if (xMirror) Color.GREEN else Color.RED
        mirrorX.onHover { mirrorX.backgroundColor = Color(0.15f) }
        mirrorX.onExit { mirrorX.backgroundColor = Color(0.1f) }
        mirrorX.onClick { xMirror = !xMirror }

        mirrorY.y = Window.height - 320f
        mirrorY.outlineColor = if (yMirror) Color.GREEN else Color.RED
        mirrorY.onHover { mirrorY.backgroundColor = Color(0.15f) }
        mirrorY.onExit { mirrorY.backgroundColor = Color(0.1f) }
        mirrorY.onClick { yMirror = !yMirror }

        mirrorZ.y = Window.height - 320f
        mirrorZ.outlineColor = if (zMirror) Color.GREEN else Color.RED
        mirrorZ.onHover { mirrorZ.backgroundColor = Color(0.15f) }
        mirrorZ.onExit { mirrorZ.backgroundColor = Color(0.1f) }
        mirrorZ.onClick { zMirror = !zMirror }

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

        shiftText.render()
        shiftXP.render()
        shiftXN.render()

        shiftYP.render()
        shiftYN.render()

        shiftZP.render()
        shiftZN.render()

        mirrorText.render()
        mirrorX.render()
        mirrorY.render()
        mirrorZ.render()
    }
}