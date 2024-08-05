package dev.blackoutburst.sve.utils

import org.lwjgl.system.MemoryStack

inline fun <R> stack(size: Int = 32 * 1024, block: (MemoryStack) -> R): R {
    val stack = MemoryStack.create(size)
    stack.push()
    stack.use { return block(it) }
}