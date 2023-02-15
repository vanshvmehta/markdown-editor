package net.codebot.application
import javafx.scene.Node
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import java.io.File

class FolderView {
    fun build(pathname: String = "src/main/kotlin") : StackPane {
        val folder_img = ImageView(Image("https://png.pngtree.com/png-vector/20190916/ourmid/pngtree-folder-icon-for-your-project-png-image_1731079.jpg"))
        folder_img.fitWidth = 20.0
        folder_img.fitHeight = 20.0
        val folderIcon: Node = folder_img
        val rootItem = TreeItem<Any?>("Inbox", folderIcon)
        rootItem.isExpanded = true
        File(pathname).walk(FileWalkDirection.BOTTOM_UP).forEach {
            val item = TreeItem<Any?>(it)
            rootItem.children.add(item)
        }
        return StackPane(TreeView<Any?>(rootItem))
    }
}