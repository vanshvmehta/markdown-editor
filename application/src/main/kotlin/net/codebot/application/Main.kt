package net.codebot.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        val bold = Button("B")
        val italics = Button("I")
        val heading = Button("H")
        val strikethrough = Button("S")
        val compileMd = Button("Compile")


        val toolbar = ToolBar(

            bold,
            italics,
            heading,
            strikethrough,
            compileMd
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
        val display_text = TextArea()
        display_text.isWrapText = true
        display_text.text = "Uneditable Text"
        display_text.font = Font("Helvetica", 12.0)
        display_text.prefColumnCount = 200
        display_text.isEditable = false
        val right = HBox(display_text)
        right.prefWidth = 750.0

        bold.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "strong text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("**" + currentHighlight + "**");
        }
        italics.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "emphasized text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("*" + currentHighlight + "*");
        }

        heading.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "Heading"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("##" + currentHighlight);
        }
        strikethrough.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "strikethrough text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("~~" + currentHighlight + "~~");
        }
        compileMd.setOnMouseClicked {
            display_text.text = text.text
        }

        bold.setTooltip( Tooltip("Bold - Meta+Shift+B"))
        italics.setTooltip( Tooltip("Italic - Meta+Shift+I"))
        heading.setTooltip( Tooltip("Heading - Meta+Shift+H"))
        strikethrough.setTooltip( Tooltip("Strikethrough - Meta+Shift+S"))
        compileMd.setTooltip( Tooltip("Compile Markdown - Meta+R"))

        val border = BorderPane()

        val topContainer = VBox()
        val mainMenu = MenuBar()

        val file = Menu("File")
        val openFile = MenuItem("Open File")
        val new = MenuItem("New")
        val save = MenuItem("Save")
        val exitApp = MenuItem("Exit")
        file.items.addAll(openFile, new, save, exitApp)

        val edit = Menu("Edit")
        val cut = MenuItem("Cut")
        val copy = MenuItem("Copy")
        val paste = MenuItem("Paste")
        edit.items.addAll(cut, copy, paste)

        //Create SubMenu Help.
        //Create SubMenu Help.
        val view = Menu("View")
        val themes = MenuItem("Themes")
        view.items.add(themes)

        mainMenu.getMenus().addAll(file, edit);

        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolbar);

        border.top = topContainer
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