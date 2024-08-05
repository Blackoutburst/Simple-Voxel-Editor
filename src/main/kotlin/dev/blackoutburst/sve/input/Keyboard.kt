package dev.blackoutburst.sve.input

object Keyboard {

    const val RELEASE = 0
    const val PRESS = 1
    const val DOWN = 2

    val keys = mutableMapOf<Int, Int>()

    fun update() {
        keys.forEach { (key, value) ->
            if (value == PRESS) {
                keys[key] = DOWN
            }
        }
    }

    fun isKeyPressed(key: Int): Boolean {
        return keys[key] == PRESS
    }

    fun isKeyReleased(key: Int): Boolean {
        return keys[key] == RELEASE
    }

    fun isKeyDown(key: Int): Boolean {
        return keys[key] == DOWN
    }
}
