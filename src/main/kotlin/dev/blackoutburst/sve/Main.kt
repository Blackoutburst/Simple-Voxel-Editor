package dev.blackoutburst.sve

import dev.blackoutburst.sve.Main.model
import dev.blackoutburst.sve.Main.queue
import dev.blackoutburst.sve.camera.Camera
import dev.blackoutburst.sve.camera.Camera.view
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.files.FileExplorer
import dev.blackoutburst.sve.graphics.Axys
import dev.blackoutburst.sve.graphics.Grid
import dev.blackoutburst.sve.graphics.Model
import dev.blackoutburst.sve.io.SVEFiles
import dev.blackoutburst.sve.ui.LeftPanel
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import java.util.concurrent.ConcurrentLinkedQueue
import dev.blackoutburst.sve.utils.main
import dev.blackoutburst.sve.graphics.Text


object Main {
    var model: Model? = null
    val queue: ConcurrentLinkedQueue<() -> Unit> = ConcurrentLinkedQueue()
}

fun main() {
    Window
    update()
}

fun update() {
    model = Model()

    SVEFiles.export(model!!)

    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glEnable(GL_BLEND)

    while (Window.isOpen) {
        while(queue.isNotEmpty()) queue.poll()?.invoke()

        Window.clear()

        Grid.update()
        Axys.update()
        LeftPanel.update()
        Camera.update()

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_E)) {
            FileExplorer.pickFile { if (it != null) main { model = SVEFiles.load(it) } }
        }

        glEnable(GL_DEPTH_TEST)

        Axys.render()
        Grid.render()

        model!!.render()

        glDisable(GL_DEPTH_TEST)
        LeftPanel.render()

        Window.update()
    }

    Window.destroy()
}
