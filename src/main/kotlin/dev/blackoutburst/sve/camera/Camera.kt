package dev.blackoutburst.sve.camera

import dev.blackoutburst.sve.Main
import dev.blackoutburst.sve.graphics.Voxel
import dev.blackoutburst.sve.input.Keyboard
import dev.blackoutburst.sve.input.Mouse
import dev.blackoutburst.sve.maths.Matrix
import dev.blackoutburst.sve.maths.Vector2f
import dev.blackoutburst.sve.maths.Vector3f
import dev.blackoutburst.sve.maths.Vector3i
import dev.blackoutburst.sve.maths.Vector4f
import dev.blackoutburst.sve.ui.LeftPanel
import dev.blackoutburst.sve.utils.Color
import dev.blackoutburst.sve.utils.RayCastResult
import dev.blackoutburst.sve.window.Window
import org.lwjgl.glfw.GLFW
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sign
import kotlin.math.sin

object Camera {
    private var lastMousePosition = Mouse.position
    private val sensitivity = 0.2f

    var position = Vector3f(0f, 0f, 5f)

    var positionOffset = Vector3f(0f, 0f, 0f)

    var rotation = Vector2f(40f, 30f)

    var view = Matrix().translate(position)
    var projection = Matrix().projectionMatrix(90f, 1000f, 0.1f)
    var projection2D = Matrix().ortho2D(0f, Window.width.toFloat(), 0f, Window.height.toFloat(), -1f, 1f)

    val ray: Vector3f
        get() {
            val mouseXNDC = (2.0 * Mouse.position.x / Window.width) - 1.0
            val mouseYNDC = 1.0 - (2.0 * Mouse.position.y / Window.height)
            val rayClip = Vector4f(mouseXNDC.toFloat(), mouseYNDC.toFloat(), -1.0f, 1.0f)

            val inverseProjection = projection.copy().invert()
            val inverseView = view.copy().invert()

            val rayEye = inverseProjection.transform(rayClip)
            rayEye.z = -1.0f
            rayEye.w = 0.0f

            val rayWorld = inverseView.transform(Vector4f(rayEye.x, rayEye.y, rayEye.z, rayEye.w))

            return Vector3f(rayWorld.x, rayWorld.y, rayWorld.z).normalize()
        }

    private fun getSpacePosition(): Vector3f {
        val inverseView = view.copy().invert()
        return Vector3f(inverseView.m30, inverseView.m31, inverseView.m32)
    }

    fun update() {
        if (LeftPanel.clicked) return

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_R)) {
            position.set(0f, 0f, 5f)
            positionOffset.set(0f, 0f, 0f)
            rotation.set(40f, 30f)
        }

        val mousePosition = Mouse.position

        var xOffset = mousePosition.x - lastMousePosition.x
        var yOffset = mousePosition.y - lastMousePosition.y
        xOffset *= sensitivity
        yOffset *= sensitivity

        lastMousePosition = mousePosition.copy()


        position.z -= Mouse.scroll / 2f
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            rotate(xOffset, yOffset)
            move(xOffset, yOffset)
        } else {
            click()
        }

        view.setIdentity()
            .translate(0f, 0f, -position.z)
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), 1f, 0f, 0f)
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), 0f, 1f, 0f)
            .translate(-position.x + positionOffset.x, -position.y + positionOffset.y, positionOffset.z)
    }

    private fun click() {
        if (Mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
            val result = dda(getSpacePosition(), ray, 50)
            result.block?.let { b ->
                val voxels = mutableListOf<Voxel>()
                voxels.add(b)

                if (LeftPanel.xMirror) {
                    val vx = Main.model!!.getVoxelByPosition(Vector3f(-(b.position.x) - 1f, b.position.y, b.position.z))
                    if (vx != null) voxels.add(vx)
                }
                if (LeftPanel.yMirror) {
                    val vy = Main.model!!.getVoxelByPosition(Vector3f(b.position.x, -(b.position.y) - 1f, b.position.z))
                    if (vy != null) voxels.add(vy)
                }
                if (LeftPanel.zMirror) {
                    val vz = Main.model!!.getVoxelByPosition(Vector3f(b.position.x, b.position.y, -(b.position.z) - 1f))
                    if (vz != null) voxels.add(vz)
                }

                if (LeftPanel.xMirror && LeftPanel.yMirror) {
                    val vxy = Main.model!!.getVoxelByPosition(Vector3f(-(b.position.x) - 1f, -(b.position.y) - 1f, b.position.z))
                    if (vxy != null) voxels.add(vxy)
                }
                if (LeftPanel.xMirror && LeftPanel.zMirror) {
                    val vxz = Main.model!!.getVoxelByPosition(Vector3f(-(b.position.x) - 1f, b.position.y, -(b.position.z) - 1f))
                    if (vxz != null) voxels.add(vxz)
                }
                if (LeftPanel.yMirror && LeftPanel.zMirror) {
                    val vyz = Main.model!!.getVoxelByPosition(Vector3f(b.position.x, -(b.position.y) - 1f, -(b.position.z) - 1f))
                    if (vyz != null) voxels.add(vyz)
                }

                if (LeftPanel.xMirror && LeftPanel.yMirror && LeftPanel.zMirror) {
                    val vxyz = Main.model!!.getVoxelByPosition(Vector3f(-(b.position.x) - 1f, -(b.position.y) - 1f, -(b.position.z) - 1f))
                    if (vxyz != null) voxels.add(vxyz)
                }

                Main.model!!.removeVoxels(voxels)
            }
        }

        if (Mouse.isButtonPressed(Mouse.RIGHT_BUTTON)) {
            val result = dda(getSpacePosition(), ray, 50)
            result.block?.let { b ->
                result.face?.let { f ->
                    val voxels = mutableListOf<Voxel>()
                    voxels.add(Voxel(b.position + f.toFloat(), LeftPanel.selectedColor))

                    if (LeftPanel.xMirror) {
                        voxels.add(Voxel(Vector3f(-(b.position.x + f.x) - 1f, b.position.y + f.y, b.position.z + f.z), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.yMirror) {
                        voxels.add(Voxel(Vector3f(b.position.x + f.x, -(b.position.y + f.y) - 1f, b.position.z + f.z), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.zMirror) {
                        voxels.add(Voxel(Vector3f(b.position.x + f.x, b.position.y + f.y, -(b.position.z + f.z) - 1f), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.xMirror && LeftPanel.yMirror) {
                        voxels.add(Voxel(Vector3f(-(b.position.x + f.x) - 1f, -(b.position.y + f.y) - 1f, b.position.z + f.z), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.xMirror && LeftPanel.zMirror) {
                        voxels.add(Voxel(Vector3f(-(b.position.x + f.x) - 1f, b.position.y + f.y, -(b.position.z + f.z) - 1f), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.yMirror && LeftPanel.zMirror) {
                        voxels.add(Voxel(Vector3f(b.position.x + f.x, -(b.position.y + f.y) - 1f, -(b.position.z + f.z) - 1f), LeftPanel.selectedColor))
                    }

                    if (LeftPanel.xMirror && LeftPanel.yMirror && LeftPanel.zMirror) {
                        voxels.add(Voxel(Vector3f(-(b.position.x + f.x) - 1f, -(b.position.y + f.y) - 1f, -(b.position.z + f.z) - 1f), LeftPanel.selectedColor))
                    }

                    Main.model!!.addVoxels(voxels)
                }
            }
        }

        if (Mouse.isButtonPressed(Mouse.MIDDLE_BUTTON)) {
            val result = dda(getSpacePosition(), ray, 50)
            result.block?.let { b ->
                LeftPanel.selectedColor = b.color.copy()
            }
        }
    }

    private fun rotate(xOffset: Float, yOffset: Float) {
        if (!Mouse.isButtonDown(Mouse.LEFT_BUTTON)) return

        rotation.x += xOffset
        rotation.y += yOffset

        if (rotation.y > 90.0f) rotation.y = 90.0f
        if (rotation.y < -90.0f) rotation.y = -90.0f
    }

    private fun move(xOffset: Float, yOffset: Float) {
        if (!Mouse.isButtonDown(Mouse.RIGHT_BUTTON)) return

        positionOffset.x += cos(-rotation.x * Math.PI / 180).toFloat() * xOffset / 20f
        positionOffset.z -= sin(-rotation.x * Math.PI / 180).toFloat() * xOffset / 20f

        positionOffset.y -= yOffset / 20f

    }

    fun dda(rayPos: Vector3f, rayDir: Vector3f, maxRaySteps: Int): RayCastResult {
        val mapPos = Vector3i(floor(rayPos.x).toInt(), floor(rayPos.y).toInt(), floor(rayPos.z).toInt())
        val rayDirLength = rayDir.length()
        val deltaDist = Vector3f(abs(rayDirLength / rayDir.x), abs(rayDirLength / rayDir.y), abs(rayDirLength / rayDir.z))
        val rayStep = Vector3i(sign(rayDir.x).toInt(), sign(rayDir.y).toInt(), sign(rayDir.z).toInt())
        val signRayDir = Vector3f(sign(rayDir.x), sign(rayDir.y), sign(rayDir.z))
        val mapPosVec3 = Vector3f(mapPos.x.toFloat(), mapPos.y.toFloat(), mapPos.z.toFloat())
        val sideDist = (signRayDir * (mapPosVec3 - rayPos) + (signRayDir * 0.5f) + 0.5f) * deltaDist
        var mask = Vector3i()

        for (i in 0 until maxRaySteps) {
            val block = getVoxelAt(mapPos)
            if (block != null) return RayCastResult(block, mask)

            if (sideDist.x < sideDist.y) {
                if (sideDist.x < sideDist.z) {
                    sideDist.x += deltaDist.x
                    mapPos.x += rayStep.x
                    mask = Vector3i(-rayStep.x, 0, 0)
                } else {
                    sideDist.z += deltaDist.z
                    mapPos.z += rayStep.z
                    mask = Vector3i(0, 0, -rayStep.z)
                }
            } else {
                if (sideDist.y < sideDist.z) {
                    sideDist.y += deltaDist.y
                    mapPos.y += rayStep.y
                    mask = Vector3i(0, -rayStep.y, 0)
                } else {
                    sideDist.z += deltaDist.z
                    mapPos.z += rayStep.z
                    mask = Vector3i(0, 0, -rayStep.z)
                }
            }
        }

        return RayCastResult(null, mask)
    }

    fun getVoxelAt(position: Vector3i): Voxel? =
        Main.model!!.voxels.find {
            it.position.x == position.x.toFloat() &&
            it.position.y == position.y.toFloat() &&
            it.position.z == position.z.toFloat()
        }
}
