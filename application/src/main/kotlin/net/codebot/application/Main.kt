package net.codebot.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
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

        val text = TextArea()
        text.isWrapText = true
        text.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        text.font = Font("Helvetica", 12.0)
        val center = HBox(text)

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
        stage.isResizable = true
        stage.width = 750.0
        stage.height = 450.0
        stage.title = "Markdown Editor"
        stage.scene = scene
        stage.show()
    }
}