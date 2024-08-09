package dev.blackoutburst.sve.io

import dev.blackoutburst.sve.graphics.Model
import dev.blackoutburst.sve.graphics.Voxel
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.utils.Color
import java.io.File
import java.io.PrintWriter

object SVEFiles {

    fun export(filePath: String, model: Model) {
        PrintWriter(filePath).use { writer ->
            model.voxels.forEach {
                writer.write("${it.position.x} ${it.position.y} ${it.position.z} ${it.color.r} ${it.color.g} ${it.color.b} ${it.color.a}\n")
            }
        }
    }

    fun load(filePath: String): Model {
        val model = Model()

        return try {
            File(filePath).forEachLine { line ->
                val split = line.replace("\n", "").split(" ")
                val x = split[0].toFloat()
                val y = split[1].toFloat()
                val z = split[2].toFloat()

                val r = split[3].toFloat()
                val g = split[4].toFloat()
                val b = split[5].toFloat()
                val a = split[6].toFloat()

                val voxel = Voxel(Vector3f(x, y, z), Color(r, g, b, a))
                model.voxels.add(voxel)
            }

            model.updateModel()

            model
        } catch (e: Exception) {
            e.printStackTrace()
            model
        }
    }
}