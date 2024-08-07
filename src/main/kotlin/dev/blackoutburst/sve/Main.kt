package dev.blackoutburst.sve

import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.files.FileExplorer
import dev.blackoutburst.sve.graphics.Axys
import dev.blackoutburst.sve.graphics.Grid
import dev.blackoutburst.sve.graphics.Model
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*

fun main() {
    Window
    update()
}

fun update() {
    val model = Model()

    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glEnable(GL_BLEND)

    while (Window.isOpen) {
        Window.clear()

        Camera.update()

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_E)) {
            FileExplorer.pickFile {}
        }

        Axys.render()
        Grid.render()

        model.render()

        Window.update()
    }

    Window.destroy()
}
