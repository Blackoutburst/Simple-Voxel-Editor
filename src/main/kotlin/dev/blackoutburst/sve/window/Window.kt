package dev.blackoutburst.sve.window

import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Vector2i
import dev.blackoutburst.sve.utils.IOUtils
import dev.blackoutburst.sve.utils.Time
import dev.blackoutburst.sve.utils.stack
import dev.blackoutburst.sve.window.callbacks.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.Platform
import java.nio.ByteBuffer
import kotlin.system.exitProcess

object Window {
    val title = MemoryStack.stackPush().UTF8("Simple Voxel Editor")
    var id = -1L
    var isOpen = false

    val width: Int
        get() = getFrameBufferSize().x
    val height: Int
        get() = getFrameBufferSize().y

    init {
        glfwInit()
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        id = glfwCreateWindow(1280, 720, title, NULL, NULL)
        if (id == -1L) exitProcess(-1)

        glfwMakeContextCurrent(id)
        createCapabilities()

        glfwSwapInterval(GLFW_TRUE)

        glClearColor(0.1f, 0.1f, 0.1f, 1f)
        glLineWidth(2.0f)
        glPointSize(2.0f)

        setCallbacks()
        setIcons()

        isOpen = true
    }

    fun update() {
        isOpen = !(glfwWindowShouldClose(id))

        Time.updateDelta()
        Mouse.update()
        Keyboard.update()

        glfwSwapBuffers(id)
        glfwPollEvents()
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
    }

    fun destroy() {
        glfwFreeCallbacks(id)
        glfwDestroyWindow(id)
        glfwTerminate()
    }

    private fun setCallbacks() {
        glfwSetWindowSizeCallback(id, WindowCallBack())
        glfwSetScrollCallback(id, MouseScrollCallBack())
        glfwSetCursorPosCallback(id, MousePositionCallBack())
        glfwSetKeyCallback(id, KeyboardCallBack())
        glfwSetMouseButtonCallback(id, MouseButtonCallBack())
        glfwSetCharCallback(id, KeyboardCharCallBack())
    }

    private fun setIcons() {
        if (Platform.get() == Platform.MACOSX) return

        val image = GLFWImage.malloc()
        val buffer = GLFWImage.malloc(1)
        try {
            image[256, 256] = loadIcon()
            buffer.put(0, image)
            glfwSetWindowIcon(id, buffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun loadIcon(): ByteBuffer {
        var image: ByteBuffer? = null

        stack {
            val comp = it.mallocInt(1)
            val w = it.mallocInt(1)
            val h = it.mallocInt(1)
            val img = IOUtils.ioResourceToByteBuffer("icon.png", 8 * 1024)

            image = STBImage.stbi_load_from_memory(img, w, h, comp, 4)
            if (image == null) {
                throw Exception("Failed to load icons")
            }
        }

        return image!!
    }

    private fun getFrameBufferSize(): Vector2i {
        val size = Vector2i()
        stack {
            val width = it.mallocInt(1)
            val height = it.mallocInt(1)

            glfwGetFramebufferSize(id, width, height)
            size.set(width[0], height[0])
        }

        return size
    }
}