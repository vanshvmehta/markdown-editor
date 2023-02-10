package net.codebot.application

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage


class Main : Application() {
    override fun start(stage: Stage) {
        // code for toolbar
        val toolbar =ToolBar(
            Button("New"),
            Button("Open"),
            Button("Save")
        )

        // code for center area
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
        text.prefColumnCount = 200
        val center = HBox(text)
        center.minWidth = 400.0

        // code for status bar (bottom pane)
        val label = Label("Test for Status Bar")
        val status = HBox(label)

        // code for left pane
        val left = VBox(Text("Left Pane"))

        // code for right pane
        val display_text = TextArea()
        display_text.isWrapText = true
        display_text.text = "Uneditable Text"
        display_text.font = Font("Helvetica", 12.0)
        display_text.prefColumnCount = 200
        display_text.isEditable = false
        val right = HBox(display_text)
        right.prefWidth = 750.0

        val border = BorderPane()
        border.top = toolbar
        border.center = center
        border.bottom = status
        border.left = left
        border.right = right

        val scene = Scene(border)
        stage.isResizable = true
        stage.width = 750.0
        stage.height = 450.0
        stage.title = "Markdown Editor"
        stage.scene = scene
        stage.show()
    }
}