package dev.blackoutburst.sve.files

import dev.blackoutburst.sve.utils.default
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.FileChooser
import javafx.stage.Stage

class FileExplorer : Application() {
    companion object {
        private var primaryStage: Stage? = null
        private var callback: (String?) -> Unit = {}

        fun init() {
            default { launch(FileExplorer::class.java) }
        }

        fun pickFile(callback: (String?) -> Unit) {
            Companion.callback = callback

            Platform.runLater { showOpenDialog() }
        }

        fun saveFile(callback: (String?) -> Unit) {
            Companion.callback = callback

            Platform.runLater { showSaveDialog() }
        }

        private fun showSaveDialog() {
            val fileChooser = FileChooser()
            fileChooser.title = "Save Project"
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("SVE Files", "*.sve"))

            val file = fileChooser.showSaveDialog(primaryStage)
            primaryStage!!.close()
            callback(file?.absolutePath)
        }

        private fun showOpenDialog() {
            val fileChooser = FileChooser()
            fileChooser.title = "Select Data File"
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("SVE Files", "*.sve"))

            val file = fileChooser.showOpenDialog(primaryStage)
            primaryStage!!.close()
            callback(file?.absolutePath)
        }
    }

    override fun start(primaryStage: Stage) {
        Companion.primaryStage = primaryStage
    }
}

