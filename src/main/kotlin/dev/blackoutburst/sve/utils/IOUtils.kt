package dev.blackoutburst.sve.utils

import org.lwjgl.BufferUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.max

object IOUtils {
    private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
        val newBuffer = BufferUtils.createByteBuffer(newCapacity)
        (buffer as Buffer).flip()
        newBuffer.put(buffer)
        return newBuffer
    }

    @Throws(IOException::class)
    fun ioResourceToByteBuffer(resource: String, bufferSize: Int): ByteBuffer {
        var buffer: ByteBuffer
        val url = Thread.currentThread().contextClassLoader.getResource(resource)
            ?: throw IOException("Classpath resource not found: $resource")
        val file = File(url.file)
        if (file.isFile) {
            val fis = FileInputStream(file)
            val fc = fis.channel
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
            fc.close()
            fis.close()
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSize)
            val source = url.openStream() ?: throw FileNotFoundException(resource)

            source.use {
                val buf = ByteArray(8192)
                while (true) {
                    val bytes = it.read(buf, 0, buf.size)
                    if (bytes == -1) break
                    if (buffer.remaining() < bytes) buffer = resizeBuffer(
                        buffer,
                        max(
                            (buffer.capacity() * 2).toDouble(),
                            (buffer.capacity() - buffer.remaining() + bytes).toDouble()
                        ).toInt()
                    )
                    buffer.put(buf, 0, bytes)
                }
                (buffer as Buffer).flip()
            }
        }
        return buffer
    }
}