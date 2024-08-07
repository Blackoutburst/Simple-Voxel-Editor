package dev.blackoutburst.sve.shader

import org.lwjgl.opengl.GL20.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

class Shader(shaderType: Int, filePath: String) {
    var id = -1

    init {
        val shaderSource = StringBuilder()
        val stream = Shader::class.java.getResourceAsStream(filePath) ?: exitProcess(-2)

        val reader = BufferedReader(InputStreamReader(stream))
        var line: String?

        while ((reader.readLine().also { line = it }) != null) {
            shaderSource.append(line).append("//\n")
        }
        reader.close()

        id = glCreateShader(shaderType)
        glShaderSource(id, shaderSource)
        glCompileShader(id)

        if (glGetShaderInfoLog(id).isNotEmpty())
            println("Error in: [$filePath] ${glGetShaderInfoLog(id)}")
    }
}
