package net.codebot.application
import javafx.scene.Node
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import java.io.File

class FolderView {
    fun build(pathname: String = "No Working Directory", dir_selected: Boolean = false) : StackPane {
        val folder_img = ImageView(Image("https://png.pngtree.com/png-vector/20190916/ourmid/pngtree-folder-icon-for-your-project-png-image_1731079.jpg"))
        folder_img.fitWidth = 20.0
        folder_img.fitHeight = 20.0
        val folderIcon: Node = folder_img
        val rootItem = TreeItem<Any?>(pathname, folderIcon)
        rootItem.isExpanded = true
        if (dir_selected) {
            val len = pathname.length
            File(pathname)
                .walk(FileWalkDirection.BOTTOM_UP)
                .maxDepth(1)
                .sortedBy { it.isDirectory }
                .forEach {
                    val item = TreeItem<Any?>(it.absolutePath.substring(len))
                    rootItem.children.add(item)
            }
            rootItem.children.removeLast()
        }
        return StackPane(TreeView<Any?>(rootItem))
    }
}