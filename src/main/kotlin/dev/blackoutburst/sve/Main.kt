package dev.blackoutburst.sve

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.files.FileExplorer
import dev.blackoutburst.sve.graphics.Model
import dev.blackoutburst.sve.graphics.Voxel
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW

fun main() {
    Window
    update()
}

fun update() {
    val model = Model()

    while (Window.isOpen) {
        Window.clear()

        Camera.update()

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_E)) {
            FileExplorer.pickFile {}
        }

        model.render()

        Window.update()
    }

    Window.destroy()
}
