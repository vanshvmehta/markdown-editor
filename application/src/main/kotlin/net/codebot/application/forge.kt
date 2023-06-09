package net.codebot.application

import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gitlab.GitLabExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.toc.TocExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension
import com.vladsch.flexmark.profile.pegdown.Extensions
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.web.WebView
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.util.converter.DoubleStringConverter
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class Forge
{
    private fun closeRequestOfMainTabPane(tab: Tab, tabPane: TabPane) {
        tab.setOnCloseRequest { e ->
            tabalert.showAndWait()
            if (tabalert.result == ButtonType.YES) {
                tabPane.getTabs().remove(
                    tabPane
                        .getSelectionModel()
                        .getSelectedItem()
                )
            } else {
                e.consume()
            }
        }
    }

    val tabalert = Alert(
        Alert.AlertType.CONFIRMATION,
        "Are you sure you want to close this tab?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
    )
    val alert = Alert(
        Alert.AlertType.CONFIRMATION,
        "Make sure to save your work before exiting. Are you sure you want to sign out?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
    )
    private fun newTabButton(tabPane: TabPane, vBox: VBox, stage: Stage ): Tab? {
        val addTab = Tab("+") // You can replace the text with an icon
        addTab.isClosable = false
        tabPane.selectionModel.selectedItemProperty()
            .addListener { observable: ObservableValue<out Tab>?, oldTab: Tab?, newTab: Tab ->
                if (newTab === addTab) {
                    var cur_file: FolderView.cur_File = FolderView.cur_File()
                    val temp = Tab("New Tab", deepcopy( stage, false, cur_file) )
                    temp.userData = cur_file
                    println("tab 2: " + temp.userData)
                    closeRequestOfMainTabPane(temp, tabPane)
                    tabPane.tabs.add(tabPane.tabs.size - 1,temp ) // Adding new tab before the "button" tab
                    tabPane.selectionModel
                        .select(tabPane.tabs.size - 2) // Selecting the tab before the button, which is the newly created one
                }
            }
        return addTab
    }
    fun deepcopy(stage: Stage, boolean: Boolean, cur_file: FolderView.cur_File): VBox {
        val border = BorderPane()

        var user = ""
        //Config, setting up themeColor and default file location
        var userConfig = initConfig(user)
        // variables to know on startup? maybe user preferences etc.
        //var cur_theme = "darkMode.css"
        var cur_theme = userConfig.theme
        // stage for login window

        val loginStage = Stage()
        //var cur_file: FolderView.cur_File = FolderView.cur_File()
        //val newborder = BorderPane()
        val bold = Button("B")
        val italics = Button("I")
        val heading = Button("H")
        val strikethrough = Button("S")
        val compileMd = Button("Compile")
        val plus = Button("+")
        val minus = Button("-")
        val combo = TextField()
        val options = MutableDataSet().set(
            Parser.EXTENSIONS, listOf(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                GitLabExtension.create(),
                EmojiExtension.create()) as Collection<Extension>
        ).toImmutable()
        val parser: Parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        combo.setTextFormatter(TextFormatter(DoubleStringConverter()))
        combo.text = "12.0"
        var htmlstr = ""
        val windows = listOf("Edit Window","Compile Window")
        val winchoice = ComboBox(
            FXCollections.observableList(windows)
        )
        winchoice.selectionModel.select("Edit Window")
        winchoice.minWidth = 120.0
        winchoice.maxWidth = 120.0
        var oldeditfont = Font.getDefault().family
        var oldeditsize = "12.0"
        var oldcompfont = Font.getDefault().family
        var oldcompsize = "15.0"
        border.userData = listOf(oldcompsize, oldcompfont)
        combo.minWidth = 60.0
        combo.maxWidth = 60.0
        val compilefont = ComboBox<String>()
        compilefont.setValue(oldcompfont)
        compilefont.items.setAll(Font.getFamilies())
        compilefont.minWidth = 100.0
        compilefont.maxWidth = 100.0

        val tabPane = TabPane()

        val toolbar = ToolBar(

            bold,
            italics,
            heading,
            strikethrough,
            compileMd,
            winchoice,
            minus,
            combo,
            plus,
            compilefont
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

        text.font = Font(oldeditfont, oldeditsize.toDouble())
        text.prefColumnCount = 200
        val center = HBox(text)
        center.minWidth = 400.0

        // code for status bar (bottom pane)
        val label = Label("Start Typing to Get Statistics!")
        val status = HBox(label)
        val getthattab = tabPane.getSelectionModel().getSelectedItem()
        var tree = StackPane()
        if(getthattab != null){
            tree = FolderView().build(getthattab, text, cur_file)
        } else {
            tree = FolderView().build( Tab(), text, cur_file)
        }



        val left = tree
        left.prefWidth = 200.0

        // code for right pane
        val webView = WebView()
        val right = webView

        fun compiledat(){
            val document: Node = parser.parse(text.text)
            var html = renderer.render(document)
            //System.out.println(html);
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
            htmlstr= html

            webView.engine.loadContent(html);
        }
        webView.engine.userStyleSheetLocation =
            "data:,body { color:#FFFFFF; background-color: #707070;" +
                    " font:" + oldcompsize + "px " + oldcompfont + "; }"
        compiledat()
        ///compilesize.valueProperty().addListener { _, _, newVal ->
        //    webView.engine.userStyleSheetLocation = "data:,body { font: " + newVal +"px " + compilefont.value + "; }";
        //}

        winchoice.valueProperty().addListener { _, _, newVal ->
            if(newVal == "Edit Window"){
                oldcompsize = combo.text
                oldcompfont = compilefont.value
                border.userData = listOf(oldcompsize, oldcompfont)
                combo.text = text.font.size.toString()
                compilefont.setValue(oldeditfont)
            }else{
                oldeditsize = combo.text
                oldeditfont = compilefont.value
                combo.text = oldcompsize
                compilefont.setValue(oldcompfont)

                if (cur_theme == "darkMode.css"){
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #707070;" +
                            " font:" + combo.text + "px " + oldcompfont + "; }"
                } else if (cur_theme == "nightBlue.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                            " font:" + combo.text + "px " + oldcompfont + "; }"
                } else {
                    webView.engine.userStyleSheetLocation = "data:,body { font: " + combo.text + "px " + oldcompfont + "; }";

                }
            }

        }


        compilefont.valueProperty().addListener { _, _, newVal ->
            if(winchoice.value == "Edit Window"){
                text.font = Font(newVal, text.font.size)
                println("hit edit window")
            }
            else {
                if (cur_theme == "darkMode.css"){
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #707070;" +
                            " font:" + combo.text + "px " + newVal + "; }"
                } else if (cur_theme == "nightBlue.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                            " font:" + combo.text + "px " + newVal + "; }"
                } else {
                    webView.engine.userStyleSheetLocation = "data:,body { font: " + combo.text + "px " + newVal + "; }";
                    //println("reached light theme")
                }
            }
        }

        combo.textProperty().addListener { _, _, newVal ->
            if(winchoice.value == "Edit Window"){
                text.font = Font(compilefont.value, newVal.toDouble())
            }else {
                val x = combo.text.toDouble()
                cur_theme = userConfig.theme
                if (cur_theme == "darkMode.css"){
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #707070;" +
                            " font:" + x.toString() + "px " + compilefont.value + "; }"
                    println("REACHED")
                } else if (cur_theme == "nightBlue.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                            " font:" + x.toString()+ "px " + compilefont.value + "; }"
                } else {
                    webView.engine.userStyleSheetLocation = "data:,body { font: " + x.toString() + "px " + compilefont.value + "; }";
                    //println("reached light theme")
                }


            }
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
        minus.setOnMouseClicked {
            if(winchoice.value == "Edit Window"){
                text.font = Font(text.font.style, text.font.size - 1)
                combo.text = text.font.size.toString()
            }else {
                val x = combo.text.toDouble() - 1
                combo.text = x.toString()
                cur_theme = userConfig.theme
                if (cur_theme == "darkMode.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #707070;" +
                            " font:" + x.toString() + "px " + compilefont.value + "; }"
                } else if (cur_theme == "nightBlue.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                            " font:" + x.toString() + "px " + compilefont.value + "; }"
                } else {
                    webView.engine.userStyleSheetLocation =
                        "data:,body { font: " + x.toString() + "px " + compilefont.value + "; }"
                }

            }

        }
        plus.setOnMouseClicked {
            if(winchoice.value == "Edit Window"){
                text.font = Font(text.font.style, text.font.size + 1)
                combo.text = text.font.size.toString()
            }else {
                val x = combo.text.toDouble() + 1
                combo.text = x.toString()
                cur_theme = userConfig.theme
                if (cur_theme == "darkMode.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #707070;" +
                            " font:" + x.toString() + "px " + compilefont.value + "; }"
                } else if (cur_theme == "nightBlue.css") {
                    webView.engine.userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                            " font:" + x.toString() + "px " + compilefont.value + "; }"
                } else {
                    webView.engine.userStyleSheetLocation =
                        "data:,body { font: " + x.toString() + "px " + compilefont.value + "; }"
                }

            }
        }


        bold.setTooltip( Tooltip("Bold - Meta+Shift+B"))
        italics.setTooltip( Tooltip("Italic - Meta+Shift+I"))
        heading.setTooltip( Tooltip("Heading - Meta+Shift+H"))
        strikethrough.setTooltip( Tooltip("Strikethrough - Meta+5"))
        compileMd.setTooltip( Tooltip("Compile Markdown - Meta+R"))


        val topContainer = VBox()
        val mainMenu = MenuBar()





        val mainCont = VBox()
        //topContainer.getChildren().add(toolbar);
        VBox.setVgrow(toolbar, Priority.ALWAYS)
        mainCont.getChildren().add(toolbar);
        VBox.setVgrow(border, Priority.ALWAYS)
        mainCont.getChildren().add(border);

        //topContainer.getChildren().add(mainCont);

        val t1 = Tab("New Tab", mainCont)
        t1.userData = cur_file
        println("tab one user data: " +  t1.userData )
        closeRequestOfMainTabPane(t1, tabPane)

        tabPane.getTabs().add(t1);
        val t2 = newTabButton(tabPane, mainCont, stage)
        if (t2 != null) {
            closeRequestOfMainTabPane(t2, tabPane)
        }





        tabPane.getTabs().add(t2);

        fun setThemes() {
            println(cur_theme)
            // clear and attach new theme
            //border.getStylesheets().clear()
            // border.getStylesheets().add(cur_theme)
            topContainer.getStylesheets().clear()
            topContainer.getStylesheets().add(cur_theme)
            userConfig = updateColorThemeConfig(userConfig, cur_theme, user)
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

            //cur_theme = userConfig.theme

            println(tabPane.getSelectionModel().getSelectedItem())
            println("Updated theme")
            println(cur_theme)
            // compiled area
            tabPane.tabs.forEach { tab ->
                var temptab = tab.content//.lookup("BorderPane")
                if(temptab != null){
                    temptab = temptab.lookup("BorderPane")
                    if(temptab != null){
                        temptab = temptab as BorderPane
                        var tempview = temptab.right.lookup("WebView") as WebView
                        var themelist = temptab.userData as List<*>
                        if (cur_theme == "darkMode.css") {

                            tempview.engine.setUserStyleSheetLocation("data:,body { color:#FFFFFF; background-color: #707070;" +
                                    " font:" + themelist[0] + "px " + themelist[1] + "; }")
                        } else if (cur_theme == "nightBlue.css") {
                            tempview.engine.
                            userStyleSheetLocation = "data:,body { color:#FFFFFF; background-color: #203354;" +
                                    " font:" + themelist[0] + "px " + themelist[1] + "; }"
                        } else  {
                            tempview.engine.
                            setUserStyleSheetLocation("data:,body {font:" + themelist[0] + "px " + themelist[1] + ";}")
                        }
                    }

                }

            }


        }



        if(boolean) {
           ;
            val file = Menu("File")
            val openFile = MenuItem("Open File")
            val new = MenuItem("New")
            val saveAsFile = MenuItem("Save As")
            val saveFile = MenuItem("Save")
            val deleteFile = MenuItem("Delete File")
            val signOut = MenuItem("Sign Out")
            val saveas = Menu("Save As")
            val savepdf = MenuItem(".pdf")
            saveas.items.addAll(savepdf)
            val exitApp = MenuItem("Exit")
            file.items.addAll(openFile, new, saveFile, saveAsFile,deleteFile, signOut, exitApp)

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
            val themesBlue = MenuItem("Night Blue")
            val themesPurple = MenuItem("Quiet Light")
            themes.items.addAll(themesLight, themesDark, themesBlue, themesPurple)
            view.items.add(themes)

            mainMenu.getMenus().addAll(file, edit, view);
            topContainer.getChildren().add(mainMenu);
            VBox.setVgrow(tabPane, Priority.ALWAYS)
            topContainer.getChildren().add(tabPane)

            openFile.accelerator = KeyCombination.keyCombination("Ctrl+O")
            new.accelerator = KeyCombination.keyCombination("Ctrl+N")
            saveFile.accelerator = KeyCombination.keyCombination("Ctrl+S")
            saveAsFile.accelerator = KeyCombination.keyCombination("Ctrl+Shift+S")
            undo.accelerator = KeyCombination.keyCombination("Ctrl+Z")
            redo.accelerator = KeyCombination.keyCombination("Ctrl+Y")
            cut.accelerator = KeyCombination.keyCombination("Ctrl+X")
            copy.accelerator = KeyCombination.keyCombination("Ctrl+C")
            paste.accelerator = KeyCombination.keyCombination("Ctrl+V")



            //OpenFile function
            openFile.onAction = EventHandler {
                val filechooser = FileChooser();
                filechooser.setTitle("Open my file")

                if (userConfig.defaultFileLocation == "user.home") {
                    filechooser.setInitialDirectory(File(System.getProperty(userConfig.defaultFileLocation)))
                } else {
                    filechooser.setInitialDirectory(File(userConfig.defaultFileLocation))
                }

                val selectedFile = filechooser.showOpenDialog(Stage())
                try {
                    val scanner = Scanner(selectedFile);
                    val temp4 = tabPane.getSelectionModel().getSelectedItem()
                    val temp = temp4. content.lookup("BorderPane")
                    val temp3 = tabPane.getSelectionModel().getSelectedItem().userData as FolderView.cur_File

                    if (temp != null && temp4 != null){
                        var temp = temp as BorderPane
                        //temp.center.lookup("TextArea")
                        var temp2 = temp.center.lookup("TextArea") as TextArea
                        temp2.clear()
                        //text.clear()
                        while (scanner.hasNextLine()) {
                            temp2.appendText(scanner.nextLine() + "\n");
                        }
                        tabPane.getSelectionModel().getSelectedItem().text = selectedFile.name
                        temp.left = FolderView().build(temp4,temp2,
                            temp3, selectedFile.parentFile.absolutePath,true)
                        temp.left.getStyleClass().add("folder-view")
                        temp3.path2file = selectedFile.absolutePath
                        //userConfig = updateFileLocationConfig(userConfig, selectedFile.parentFile.absolutePath)
                    }

                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }

            }

            //SaveFile Function
            saveFile.onAction = EventHandler {

                val temp3 = tabPane.getSelectionModel().getSelectedItem().userData as FolderView.cur_File
                println("savefile path: "+temp3)
                if (temp3.path2file != null) {
                    try {
                        val printWriter = PrintWriter(temp3.path2file)
                        val temp = tabPane.getSelectionModel().getSelectedItem().
                        content.lookup("BorderPane")

                        if (temp != null){
                            var temp = temp as BorderPane
                            //temp.center.lookup("TextArea")
                            var temp2 = temp.center.lookup("TextArea") as TextArea
                            //tabPane.getSelectionModel().getSelectedItem().text = cur_file.path2file
                            printWriter.write(temp2.text);
                            printWriter.close();


                        }

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace();
                    }
                    println("Attempting to update your file remotely!")
                    updateFile(user,temp3.path2file)
                }
            }

            //zAsFile function
            saveAsFile.onAction = EventHandler {
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
                        val temp = tabPane.getSelectionModel().getSelectedItem().content.lookup("BorderPane")

                        if (temp != null) {
                            var temp = temp as BorderPane
                            //temp.center.lookup("TextArea")
                            var temp2 = temp.center.lookup("TextArea") as TextArea
                            tabPane.getSelectionModel().getSelectedItem().text = file.name
                            printWriter.write(temp2.text);
                            printWriter.close();
                            /*
                            temp.left = FolderView().build(text, cur_file, file.parentFile.absolutePath,true)
                            temp.left.getStyleClass().add("folder-view")*/
                            val temp3 = tabPane.getSelectionModel().getSelectedItem().userData as FolderView.cur_File
                            if (temp3 != null) {
                                temp3.path2file = file.absolutePath
                                //userConfig = updateFileLocationConfig(userConfig, file.parentFile.absolutePath)
                            }

                        }

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace();
                    }
                    println("Attempting to upload your file remotely!")
                    uploadFile(user, file)
                }
            }

        deleteFile.onAction = EventHandler {// have to close file first
            val temp3 = tabPane.getSelectionModel().getSelectedItem().userData as FolderView.cur_File

            if (temp3.path2file != null) {
                println("Attempting to delete your file remotely!")
                delFile(user, temp3.path2file)
                try {
                    val result = Files.deleteIfExists(Paths.get(temp3.path2file))

                    if (result) {
                        println("Deletion succeeded.")
                    } else {
                        println("file trying to delete: " + temp3.path2file)
                        println("Deletion failed.")
                    }
                } catch (e: IOException) {
                    println("Deletion failed.")
                    e.printStackTrace()
                }
                if(tabPane.tabs.size == 2){
                    var cur_file: FolderView.cur_File = FolderView.cur_File()
                    val temp = Tab("New Tab", deepcopy( stage, false, cur_file) )
                    temp.userData = cur_file
                    //println("tab 2: " + temp.userData)
                    closeRequestOfMainTabPane(temp, tabPane)
                    tabPane.tabs.add(tabPane.tabs.size - 1,temp ) // Adding new tab before the "button" tab
                    tabPane.tabs.remove(tabPane.getSelectionModel().getSelectedItem())
                    tabPane.selectionModel
                        .select(tabPane.tabs.size - 3) // Se

                } else {
                    tabPane.tabs.remove(tabPane.getSelectionModel().getSelectedItem())
                }



            }
        }
        new.onAction = EventHandler {
            var cur_file: FolderView.cur_File = FolderView.cur_File()
            val temp = Tab("New Tab", deepcopy( stage, false, cur_file) )
            temp.userData = cur_file
            //println("tab 2: " + temp.userData)
            closeRequestOfMainTabPane(temp, tabPane)
            tabPane.tabs.add(tabPane.tabs.size - 1,temp ) // Adding new tab before the "button" tab
            tabPane.selectionModel
                .select(tabPane.tabs.size - 2) // Se
        }

        signOut.onAction = EventHandler {

            alert.showAndWait()
            if (alert.result == ButtonType.YES) {
                stage.hide()
                var cur_file2: FolderView.cur_File = FolderView.cur_File()
                stage.scene = Scene(Forge().deepcopy(stage,true, cur_file2))
                var rootPath = Paths.get(System.getProperty("user.home"))
                val partialPath = Paths.get(".MarkDown/" + user)
                val resolvedPath: Path = rootPath.resolve(partialPath)
                deleteDirectory(resolvedPath.toString())
                user = ""
                rootPath = Paths.get(System.getProperty("user.home"))
                val configFile = File(rootPath.resolve(Paths.get(".Markdown/config.txt")).toString())
                Files.deleteIfExists(configFile.toPath())
                //loginStage.show()
               // loginStage.scene = Scene(LoginManager().build(loginStage, stage))
            }

            // reset stage as well
        }
        val OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL and (Extensions.ANCHORLINKS or Extensions.EXTANCHORLINKS_WRAP).inv(), TocExtension.create()
        ).toMutable()
            .set(TocExtension.LIST_CLASS, PdfConverterExtension.DEFAULT_TOC_LIST_CLASS)
            .toImmutable()

            savepdf.onAction = EventHandler {
                var savefilechooser = FileChooser()
                if (userConfig.defaultFileLocation == "user.home") {
                    savefilechooser.setInitialDirectory(File(System.getProperty(userConfig.defaultFileLocation)))
                } else {
                    savefilechooser.setInitialDirectory(File(userConfig.defaultFileLocation))
                }
                val strfile = savefilechooser.initialDirectory.toString()
                //val html = webView.engine.executeScript("document.documentElement.outerHTML")//.toString()
                PdfConverterExtension.exportToPdf(strfile + "/test.pdf", htmlstr, "", OPTIONS)

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

        // themes helper to save font size on switch
        fun themes_font_helper() {
            if (winchoice.value == "Edit Window") {
                oldeditsize = combo.text
                oldeditfont = compilefont.value
            } else {
                oldcompsize = combo.text
                oldcompfont = compilefont.value
                border.userData = listOf(oldcompsize, oldcompfont)
            }
        }

        // Themes function
        themesLight.onAction = EventHandler {
            cur_theme = "lightMode.css"
               //userConfig.theme =  cur_theme
            themes_font_helper()
            setThemes()
        }

        themesDark.onAction = EventHandler {
            cur_theme = "darkMode.css"
            themes_font_helper()
            setThemes()
        }

        themesBlue.onAction = EventHandler {
            cur_theme = "nightBlue.css"
            themes_font_helper()
            setThemes()
        }

        themesPurple.onAction = EventHandler {
            cur_theme = "quietLight.css"
            themes_font_helper()
            setThemes()
        }




            //return topContainer
        }




        // Shortcuts for Menu Items


        // Shortcuts for Buttons
        val bold_combo: KeyCombination = KeyCodeCombination(
            KeyCode.B,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val italic_combo: KeyCombination = KeyCodeCombination(
            KeyCode.I,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val heading_combo: KeyCombination = KeyCodeCombination(
            KeyCode.H,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN)
        val strikethrough_combo: KeyCombination = KeyCodeCombination(
            KeyCode.DIGIT5,
            KeyCombination.CONTROL_DOWN)
        val compile_combo: KeyCombination = KeyCodeCombination(
            KeyCode.R,
            KeyCombination.CONTROL_DOWN)

        border.setOnKeyPressed {
            when (true) {
                bold_combo.match(it) -> bold.fire()
                italic_combo.match(it) -> italics.fire()
                heading_combo.match(it) -> heading.fire()
                strikethrough_combo.match(it) -> strikethrough.fire()
                compile_combo.match(it) -> compileMd.fire()
            }
        }







        //border.top = topContainer
        border.center = center
        border.bottom = status
        border.left = left
        border.right = right
        setThemes()
        stage.width = userConfig.defaultWidth
        stage.height = userConfig.defaultHeight

        stage.widthProperty().addListener{ obs, oldValue, newValue ->
            // stage.setWidth(newValue as Double)
            userConfig = updateWidthConfig(userConfig, newValue as Double, user)
        }

        stage.heightProperty().addListener{ obs, oldValue, newValue ->
            // stage.setHeight(newValue as Double)
            userConfig = updateHeightConfig(userConfig, newValue as Double, user)
        }

        if(boolean){
            val loginScene = Scene(LoginManager().build(loginStage, stage))
            loginStage.scene = loginScene
            loginStage.title = "User Login"
            loginStage.show()

            loginStage.onHidden = EventHandler {
                user = loginStage.title
                getConfig(user)

                userConfig = initConfig(user)
                cur_theme = userConfig.theme
                setThemes()
                stage.width = userConfig.defaultWidth
                stage.height = userConfig.defaultHeight

                if (user != "") {
                    val rootPath = Paths.get(System.getProperty("user.home"))

                    val partialPath = Paths.get(".MarkDown/", user, "/root")
                    val resolvedPath: Path = rootPath.resolve(partialPath)
                    println(resolvedPath.toString())
                    userConfig = updateFileLocationConfig(userConfig, resolvedPath.toString(), user)
                } else {
                    userConfig = updateFileLocationConfig(userConfig, "user.home", user)
                }
            }



            return topContainer
        }
        return mainCont



    }
}