package net.codebot.application


import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage



class Main : Application() {
    override fun start(stage: Stage) {

        stage.isResizable = true
        var cur_file: FolderView.cur_File = FolderView.cur_File()
        stage.title = "Markdown Editor"
        stage.scene = Scene(Forge().deepcopy(stage, true, cur_file))

    }
}