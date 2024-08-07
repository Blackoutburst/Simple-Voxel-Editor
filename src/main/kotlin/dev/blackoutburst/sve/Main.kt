package dev.blackoutburst.sve

import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.files.FileExplorer
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW

fun main() {
    update()
}

fun update() {
    while (Window.isOpen) {
        Window.clear()

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_E)) {
            FileExplorer.pickFile { filePath -> }
        }

        Window.update()
    }

    Window.destroy()
}
