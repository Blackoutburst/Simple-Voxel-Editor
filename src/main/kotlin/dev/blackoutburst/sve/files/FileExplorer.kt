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

        fun pickFile(callback: (String?) -> Unit) {
            Companion.callback = callback

            if (primaryStage == null)
                default { launch(FileExplorer::class.java) }
            else
                Platform.runLater { showOpenDialog() }
        }

        fun showOpenDialog() {
            val fileChooser = FileChooser()
            fileChooser.title = "Select Data File"
            fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("SVE Files", "*.sve")
            )
            val file = fileChooser.showOpenDialog(primaryStage)
            primaryStage!!.close()
            callback(file?.absolutePath)
        }
    }

    override fun start(primaryStage: Stage) {
        Companion.primaryStage = primaryStage
        showOpenDialog()
    }
}

