package net.codebot.application

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import net.codebot.api.verifyUser

class LoginManager {
    fun build(self: Stage, markdown: Stage): StackPane {
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        val gridTitle = Text("User Login")
        grid.add(gridTitle, 0, 0, 2, 1)

        val userName = Label("User Name:")
        grid.add(userName, 0, 1)
        val userTextField = TextField()
        grid.add(userTextField, 1, 1)

        val pw = Label("Password:")
        grid.add(pw, 0, 2)
        val pwBox = PasswordField()
        grid.add(pwBox, 1, 2)

        // Defining sign in buttons and their placement
        val guestBtn = Button("Sign in as Guest")
        val userBtn = Button("Sign in")
        val hbBtn = HBox(10.0)
        hbBtn.alignment = Pos.BOTTOM_RIGHT
        hbBtn.children.addAll(guestBtn, userBtn)
        grid.add(hbBtn, 1, 4)

        val actiontarget = Text()
        grid.add(actiontarget, 1, 6)

        guestBtn.onAction = EventHandler {
            markdown.show()
            self.title = ""
            self.hide()
        }

        userBtn.onAction = EventHandler {
            actiontarget.setFill(Color.FIREBRICK)
            val user = userTextField.text
            val password = pwBox.text

            if (user == "") {
                actiontarget.setText("Enter a user name.")
            } else if (password == "") {
                actiontarget.setText("Enter a password.")
            } else {
                val verified = verifyUser(user, password)
                if (verified) {
                    markdown.show()
                    self.title = user
                    self.hide()
                    getUserDirectory(user)
                } else {
                    actiontarget.setText("Invalid password.")
                }
            }
        }
        return StackPane(grid)
    }
}