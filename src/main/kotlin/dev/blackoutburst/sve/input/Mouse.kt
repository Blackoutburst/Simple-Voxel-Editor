package dev.blackoutburst.sve.input

import dev.blackoutburst.sve.maths.Vector2f

object Mouse {
	const val LEFT_BUTTON = 0
	const val RIGHT_BUTTON = 1
	const val MIDDLE_BUTTON = 2

	const val RELEASE = 0
	const val PRESS = 1
	const val DOWN = 2

    var x: Float = 0f
    var y: Float = 0f
	var scroll: Float = 0f
    var position = Vector2f()
	val buttons = mutableMapOf<Int, Int>()

	fun update() {
		buttons.forEach { (key, value) ->
			if (value == PRESS) {
				buttons[key] = DOWN
			}
		}
		scroll = 0f
	}

	fun isButtonPressed(button: Int): Boolean {
		return buttons[button] == PRESS
	}

	fun isButtonReleased(button: Int): Boolean {
		return buttons[button] == RELEASE
	}

	fun isButtonDown(button: Int): Boolean {
		return buttons[button] == DOWN
	}
}
