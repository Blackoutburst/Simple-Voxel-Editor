package dev.blackoutburst.sve.utils

import dev.blackoutburst.sve.Main
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun main(crossinline block: () -> Unit) {
    Main.queue.add { block() }
}

inline fun io(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

inline fun default(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch {
        block()
    }
}

inline fun unconfined(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Unconfined).launch {
        block()
    }
}