package dev.blackoutburst.sve.graphics

import dev.blackoutburst.sve.utils.IOUtils
import dev.blackoutburst.sve.utils.stack
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage

class Texture {
    var id = -1

    constructor(buffer: ByteBuffer, flip: Boolean = false) {
        stack { stack ->
            id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)

            val comp = stack.mallocInt(1)
            val width = stack.mallocInt(1)
            val height = stack.mallocInt(1)

            STBImage.stbi_set_flip_vertically_on_load(flip)

            val data = STBImage.stbi_load_from_memory(
                buffer,
                width,
                height,
                comp,
                4
            )

            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width[0],
                height[0],
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            )

            comp.clear()
            data?.let { STBImage.stbi_image_free(it) }

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        }
    }

    constructor(filePath: String, flip: Boolean = true) {
        stack { stack ->
            id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)

            val comp = stack.mallocInt(1)
            val width = stack.mallocInt(1)
            val height = stack.mallocInt(1)

            STBImage.stbi_set_flip_vertically_on_load(flip)

            val data = STBImage.stbi_load_from_memory(
                IOUtils.ioResourceToByteBuffer(filePath, 4096),
                width,
                height,
                comp,
                4
            )

            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width[0],
                height[0],
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            )

            comp.clear()
            data?.let { STBImage.stbi_image_free(it) }

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        }
    }
}
