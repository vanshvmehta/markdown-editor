package net.codebot.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToolBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import net.codebot.shared.SysInfo

class Main : Application() {
    override fun start(stage: Stage) {
        val toolbar =ToolBar(
            Button("New"),
            Button("Open"),
            Button("Save")
        )

        val text = Text("Center")
        text.font = Font("Helvetica", 40.0)
        val center = VBox(text)

        val label = Label("Test for Status Bar")
        val status = HBox(label)

        val left = VBox(Text("Left Pane"))

        val right = VBox(Text("Right Pane"))

        val border = BorderPane()
        border.top = toolbar
        border.center = center
        border.bottom = status
        border.left = left
        border.right = right
        /** stage.scene = Scene(
        BorderPane(Label("Hello ${SysInfo.userName}")),
        250.0,
        150.0)
         **/
        val scene = Scene(border)
        stage.width = 250.0
        stage.height = 150.0
        stage.isResizable = false
        stage.title = "GUI Project"
        stage.scene = scene
        stage.show()
    }
}