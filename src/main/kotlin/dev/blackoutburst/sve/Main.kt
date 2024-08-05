package dev.blackoutburst.sve

import dev.blackoutburst.sve.window.Window

fun main() {
    update()
}

fun update() {
    while (Window.isOpen) {
        Window.clear()

        Window.update()
    }
}