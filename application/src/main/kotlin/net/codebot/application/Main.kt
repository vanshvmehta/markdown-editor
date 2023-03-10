package net.codebot.application

import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gitlab.GitLabExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.web.WebView
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.*


class Main : Application() {
    override fun start(stage: Stage) {
        val bold = Button("B")
        val italics = Button("I")
        val heading = Button("H")
        val strikethrough = Button("S")
        val compileMd = Button("Compile")

        val options = MutableDataSet().set(
            Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(),
                StrikethroughExtension.create(),
                GitLabExtension.create(),
                EmojiExtension.create()) as Collection<Extension>
        ).toImmutable()
        val parser: Parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()


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
        val label = Label("")
        val status = HBox(label)



        // code for left pane
        val tree = FolderView().build()
        val left = tree
        left.prefWidth = 250.0

        // code for right pane
        val webView = WebView()
        val display_text = TextArea()
        display_text.isWrapText = true
        display_text.text = "Uneditable Text"
        display_text.font = Font("Helvetica", 12.0)
        display_text.prefColumnCount = 200
        display_text.isEditable = false
        val right = webView
        // val right = HBox(display_text)
        //right.prefWidth = 650.0

        fun compiledat(){
            val document: Node = parser.parse(text.text)
            var html = renderer.render(document)
            System.out.println(html);
            html = """
                <!DOCTYPE html>
            <!-- KaTeX requires the use of the HTML5 doctype. Without it, KaTeX may not render properly -->
            <html>
            
            <head>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.4/dist/katex.min.css" integrity="sha384-vKruj+a13U8yHIkAyGgK1J3ArTLzrFGBbBc0tDp4ad/EyewESeXE/Iv67Aj8gKZ0" crossorigin="anonymous">

            <!-- The loading of KaTeX is deferred to speed up page rendering -->
            <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.4/dist/katex.min.js" integrity="sha384-PwRUT/YqbnEjkZO0zZxNqcxACrXe+j766U2amXcgMg5457rve2Y7I6ZJSm2A0mS4" crossorigin="anonymous"></script>

            <!-- To automatically render math in text elements, include the auto-render extension: -->
            <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.4/dist/contrib/auto-render.min.js" integrity="sha384-+VBxd3r6XgURycqtZ117nYw44OOcIax56Z4dCRWbxyPt0Koah1uHoK0o4+/RRE05" crossorigin="anonymous"
            onload="renderMathInElement(document.body);"></script>
            </head>""" + html + """<script>
                (function () {
                  document.addEventListener("DOMContentLoaded", function () {
                    var mathElems = document.getElementsByClassName("katex");
                    var elems = [];
                    for (const i in mathElems) {
                        if (mathElems.hasOwnProperty(i)) elems.push(mathElems[i]);
                    }
            
                    elems.forEach(elem => {
                        katex.render(elem.textContent, elem, { throwOnError: false, displayMode: elem.nodeName !== 'SPAN', });
                    });
                });
            })(); </html>"""
            webView.getEngine().loadContent(html);
        }

        text.textProperty().addListener { observable, oldValue, newValue ->
            val newlines = newValue.split("\n").size
            val spaces = newValue.split("\\s+".toRegex()).size
            label.text = "Character count: " + newValue.length + "   Line Count: " + newlines + "   Word Count: " + spaces
            compiledat()
        }

        compileMd.setOnMouseClicked {
            compiledat()
        }

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
            text.replaceSelection("## " + currentHighlight);
        }
        strikethrough.setOnMouseClicked {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "strikethrough text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("~~" + currentHighlight + "~~");
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
        val saveFile = MenuItem("Save")
        val exitApp = MenuItem("Exit")
        file.items.addAll(openFile, new, saveFile, exitApp)

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

        //OpenFile function
        openFile.onAction = EventHandler {
            val filechooser = FileChooser();
            filechooser.setTitle("Open my file");
            filechooser.setInitialDirectory(File(System.getProperty("user.home")));
            val selectedFile = filechooser.showOpenDialog(stage);
            try {
                val scanner = Scanner(selectedFile);
                text.clear()
                while (scanner.hasNextLine()) {
                    text.appendText(scanner.nextLine() + "\n");
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            }
            border.left = FolderView().build(selectedFile.parentFile.absolutePath)
        }

        //SaveFile function
        saveFile.onAction = EventHandler {
            val file = FileChooser().showSaveDialog(Stage());
            if (file != null) {
                try {
                    val printWriter = PrintWriter(file);
                    printWriter.write(text.text);
                    printWriter.close();
                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }
            }
        }

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