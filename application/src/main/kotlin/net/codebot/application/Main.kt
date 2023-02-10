package net.codebot.application

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToolBar
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import net.codebot.shared.SysInfo
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView

class Main : Application() {
    override fun start(stage: Stage) {
        val bold = Button("B")
        val italics = Button("I")
        val heading = Button("H")
        val strikethrough = Button("S")


        val toolbar =ToolBar(
            Button("New"),
            Button("Open"),
            Button("Save"),
            bold,
            italics,
            heading,
            strikethrough
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
        text.prefColumnCount = 200
        val center = HBox(text)
        center.minWidth = 400.0

        // code for status bar (bottom pane)
        val label = Label("Test for Status Bar")
        val status = HBox(label)

        // code for left pane
        val left = VBox(Text("Left Pane"))

        // code for right pane
        val right = VBox(Text("Right Pane"))
        right.prefWidth = 750.0
        right.setStyle("-fx-background-color: #FFA500;")
        right.setBackground(Background(BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)))

        bold.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if(currentHighlight == ""){
                currentHighlight = "strong text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("**"+currentHighlight+"**");
        }
        italics.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if(currentHighlight == ""){
                currentHighlight = "emphasized text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("*"+currentHighlight+"*");
        }

        heading.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if(currentHighlight == ""){
                currentHighlight = "Heading"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("##"+currentHighlight);
        }
        strikethrough.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if(currentHighlight == ""){
                currentHighlight = "strikethrough text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("~~"+currentHighlight+"~~");
        }

        bold.setTooltip( Tooltip("Bold - Meta+Shift+B"))
        italics.setTooltip( Tooltip("Italic - Meta+Shift+I"))
        heading.setTooltip( Tooltip("Heading - Meta+Shift+H"))
        strikethrough.setTooltip( Tooltip("Strikethrough - Meta+Shift+S"))

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