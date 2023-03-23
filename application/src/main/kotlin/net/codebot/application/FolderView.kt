package net.codebot.application
import javafx.scene.Node
import javafx.scene.control.TextArea
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import java.io.File
import java.io.FileNotFoundException
import java.lang.Integer.min
import java.util.*


class FolderView {
    fun build(text: TextArea, pathname: String = "No Working Directory", dir_selected: Boolean = false) : StackPane {
        // images for file directory
        val folder_icon = ImageView(Image("https://www.iconpacks.net/icons/2/free-folder-icon-1485-thumb.png"))
        val image_icon = ImageView(Image("https://cdn-icons-png.flaticon.com/512/1160/1160358.png"))
        val text_icon = ImageView(Image("https://cdn-icons-png.flaticon.com/512/32/32329.png"))
        val pdf_icon = ImageView(Image("https://icons.veryicon.com/png/o/file-type/file-type-1/pdf-icon.png"))
        val blank_icon = ImageView(Image("https://cdn-icons-png.flaticon.com/512/101/101671.png"))

        // formatting images
        folder_icon.fitWidth = 20.0
        folder_icon.fitHeight = 20.0

        // building tree
        val folderIcon: Node = folder_icon
        val rootItem = TreeItem<Any?>(pathname, folderIcon)
        rootItem.isExpanded = true
        if (dir_selected) {
            val len = pathname.length
            File(pathname)
                .walk(FileWalkDirection.BOTTOM_UP)
                .maxDepth(1)
                .sortedBy { it.isDirectory }
                .forEach {
                    var item_icon = ImageView(folder_icon.image)

                    if (it.absoluteFile.isDirectory) {
                        item_icon = ImageView(folder_icon.image)
                    } else {
                        when (it.absoluteFile.extension) {
                            "png", "svg", "jpeg" -> item_icon = ImageView(image_icon.image)
                            "txt", "md" -> item_icon = ImageView(text_icon.image)
                            "pdf" -> item_icon = ImageView(pdf_icon.image)
                            else -> item_icon = ImageView(blank_icon.image)
                        }
                    }
                    item_icon.fitWidth = 20.0
                    item_icon.fitHeight = 20.0
                    val item = TreeItem<Any?>(it.absolutePath.substring(min(it.absolutePath.length, len + 1)),
                        item_icon)
                    rootItem.children.add(item)
            }
            rootItem.children.removeLast()
        }
        val treeTable = TreeView<Any?>(rootItem)
        treeTable.getSelectionModel().selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue != null && newValue !== oldValue) {
                val path = pathname + '\\' + newValue.value

                try {
                    val scanner = Scanner(File(path));
                    text.clear()
                        while (scanner.hasNextLine()) {
                            text.appendText(scanner.nextLine() + "\n");
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }
            }
        }

        return StackPane(treeTable)
    }
}

