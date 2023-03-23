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
import javafx.scene.input.*
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

        //Config, setting up themeColor and default file location
        var userConfig = initConfig()
        // variables to know on startup? maybe user preferences etc.
        var cur_theme = "darkMode.css"

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
        text.text = "## Releases\n" +
                "// 2/16/2023\n" +
                "// Version : 1.0.0\n" +
                "Baseline functionality\n" +
                "- A resizable application\n" +
                "- Toolbars\n" +
                "- Buttons for basic Markdown syntax bold, italics etc.\n" +
                "- Text pane to type\n" +
                "- Display pane and compile button\n" +
                "- Ability to open .txt files\n" +
                "- Ability to save .txt files\n" +
                "- File directory pane"
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
        left.prefWidth = 200.0

        // code for right pane
        val webView = WebView()
        val right = webView

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

        compileMd.onAction = EventHandler {
            compiledat()
        }

        bold.onAction = EventHandler {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "strong text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("**" + currentHighlight + "**");
        }
        italics.onAction = EventHandler {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "emphasized text"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("*" + currentHighlight + "*");
        }

        heading.onAction = EventHandler {
            var currentHighlight = text.selectedText
            if (currentHighlight == "") {
                currentHighlight = "Heading"
            }
            //text.insert("**" + currentHighlight + "**", text.getCaretPosition());
            text.replaceSelection("## " + currentHighlight);
        }
        strikethrough.onAction = EventHandler {
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
        val undo = MenuItem("Undo")
        val redo = MenuItem("Redo")
        val cut = MenuItem("Cut")
        val copy = MenuItem("Copy")
        val paste = MenuItem("Paste")
        edit.items.addAll(undo, redo, cut, copy, paste)

        //Create SubMenu Help.
        //Create SubMenu Help.
        val view = Menu("View")
        val themes = Menu("Themes")
        val themesLight = MenuItem("Light Mode")
        val themesDark = MenuItem("Dark Mode")
        themes.items.addAll(themesLight, themesDark)
        view.items.add(themes)

        mainMenu.getMenus().addAll(file, edit, view);

        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolbar);

        // stylesheets for themes
        fun setThemes() {
            // clear and attach new theme
            border.getStylesheets().clear()
            border.getStylesheets().add(cur_theme)

            // menu bars
            mainMenu.getStyleClass().add("menu-bar")
            mainMenu.getStyleClass().add("menu")
            toolbar.getStyleClass().add("toolbar")

            // center text area
            text.getStyleClass().add("text-area")

            // status bar
            label.getStyleClass().add("status-text")
            status.getStyleClass().add("status-bar")

            // left stylesheets in FolderView.kt
            left.getStyleClass().add("folder-view")

            // compiled area
            webView.engine.setUserStyleSheetLocation("file:src\\main\\resources\\" + cur_theme)
        }

        // Shortcuts for Menu Items
        openFile.accelerator = KeyCombination.keyCombination("Ctrl+O")
        new.accelerator = KeyCombination.keyCombination("Ctrl+N")
        saveFile.accelerator = KeyCombination.keyCombination("Ctrl+S")
        undo.accelerator = KeyCombination.keyCombination("Ctrl+Z")
        redo.accelerator = KeyCombination.keyCombination("Ctrl+Y")
        cut.accelerator = KeyCombination.keyCombination("Ctrl+X")
        copy.accelerator = KeyCombination.keyCombination("Ctrl+C")
        paste.accelerator = KeyCombination.keyCombination("Ctrl+V")

        // Shortcuts for Buttons
        val bold_combo: KeyCombination = KeyCodeCombination(KeyCode.B,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val italic_combo: KeyCombination = KeyCodeCombination(KeyCode.I,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val heading_combo: KeyCombination = KeyCodeCombination(KeyCode.H,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val strikethrough_combo: KeyCombination = KeyCodeCombination(KeyCode.S,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val compile_combo: KeyCombination = KeyCodeCombination(KeyCode.R,
            KeyCombination.CONTROL_DOWN)

        border.setOnKeyPressed {
                when (true) {
                    bold_combo.match(it) -> strikethrough.fire()
                    italic_combo.match(it) -> italics.fire()
                    heading_combo.match(it) -> heading.fire()
                    strikethrough_combo.match(it) -> strikethrough.fire()
                    compile_combo.match(it) -> compileMd.fire()
                }
        }

        //OpenFile function
        openFile.onAction = EventHandler {
            val filechooser = FileChooser();
            filechooser.setTitle("Open my file")

            if (userConfig.defaultFileLocation == "user.home") {
                filechooser.setInitialDirectory(File(System.getProperty(userConfig.defaultFileLocation)))
            } else {
                filechooser.setInitialDirectory(File(userConfig.defaultFileLocation))
            }

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
            border.left = FolderView().build(selectedFile.parentFile.absolutePath,true)
            border.left.getStyleClass().add("folder-view")
            userConfig = updateFileLocationConfig(userConfig, selectedFile.parentFile.absolutePath)
        }

        //SaveFile function
        saveFile.onAction = EventHandler {
            val savefilechooser = FileChooser()

            if (userConfig.defaultFileLocation == "user.home") {
                savefilechooser.setInitialDirectory(File(System.getProperty(userConfig.defaultFileLocation)))
            } else {
                savefilechooser.setInitialDirectory(File(userConfig.defaultFileLocation))
            }
            val file = savefilechooser.showSaveDialog(Stage());
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

        // Undo, Redo
        undo.isDisable = true
        redo.isDisable = true

        // check if Undo and Redo should be enabled
        text.undoableProperty().addListener {obs, cannotUndo, canUndo ->
            undo.isDisable = !text.isUndoable
        }

        text.redoableProperty().addListener {obs, cannotRedo, canRedo ->
            redo.isDisable = !text.isRedoable
        }

        undo.onAction = EventHandler {
            text.undo()
        }

        redo.onAction = EventHandler {
            text.redo()
        }

        // Cut, Copy, Paste
        var clipboard: Clipboard = Clipboard.getSystemClipboard()
        var content = ClipboardContent()
        cut.isDisable = true
        copy.isDisable = true
        paste.isDisable = true

        // check if Cut, Copy, Paste should be enabled
        edit.showingProperty().addListener { obs, notShown, isShown ->
            cut.isDisable = true
            copy.isDisable = true
            paste.isDisable = true

            if (text.isFocused) {
                if (text.selectedText != "") {
                    cut.isDisable = false
                    copy.isDisable = false
                }
                paste.isDisable = !clipboard.hasString()
            }
            else if (webView.isFocused) {
                if (webView.getEngine().executeScript("window.getSelection().toString()")
                            as String? != "") {
                    copy.isDisable = false
                }
            }
        }

        cut.onAction = EventHandler {
            var currentHighlight = text.selectedText
            text.replaceSelection("")
            content.putString(currentHighlight)
            clipboard.setContent(content)
        }

        copy.onAction = EventHandler {
            var currentHighlight: String? = ""
            if (webView.isFocused) {
                currentHighlight = webView.getEngine().
                executeScript("window.getSelection().toString()") as String?
            }
            else {
                currentHighlight = text.selectedText
            }
            content.putString(currentHighlight)
            clipboard.setContent(content)
        }

        paste.onAction = EventHandler {
            text.replaceSelection(clipboard.string)
        }

        // Themes function
        themesLight.onAction = EventHandler {
            cur_theme = "lightMode.css"
            setThemes()
        }

        themesDark.onAction = EventHandler {
            cur_theme = "darkMode.css"
            setThemes()
        }

        border.top = topContainer
        border.center = center
        border.bottom = status
        border.left = left
        border.right = right

        setThemes()

        val scene = Scene(border)
        stage.isResizable = true
        stage.width = 750.0
        stage.height = 450.0
        stage.title = "Markdown Editor"
        stage.scene = scene
        stage.show()
    }
}