package net.codebot.application
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Serializable
data class UserSetting(
    var theme: String,
    var defaultFileLocation: String,
    var defaultWidth: Double,
    var defaultHeight: Double
)

//Check config file -> return user setting data class
fun initConfig(user : String): UserSetting {
    var currSetting = UserSetting(
        "","",
        0.0, 0.0)

    val configFile = File(getConfigPath(user))

    //Check if Config File exists, if not, create a new default one
    if (!configFile.exists()) {
        println("Config file not found")
        val configFolder = configFile.parentFile
        if (!configFolder.exists()) {
            println("Config folder not found")
            if (!configFolder.mkdirs()) {
                throw IllegalStateException("Couldn't create dir: " + configFolder)
            } else {
                println("Config folder created");
            }
        } else {
            println("Config folder exists")
        }
        println("Writing Default Config")
        //Default Config
        updateConfig(UserSetting(
            "light", "user.home",
            750.0, 450.0), user)
    }

    try {
        println("Reading Config from" + configFile.toString())
        val config = String(Files.readAllBytes(configFile.toPath()))
        println("Parsing Config into Json")
        val json = Json {prettyPrint = true}
        currSetting = json.decodeFromString<UserSetting>(config)
        // print(currSetting)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    if ( currSetting.theme == "" ||currSetting.defaultFileLocation == "") {
        throw IllegalStateException("Couldn't load User Config")
    }
    return currSetting
}

private fun updateConfig(userSetting: UserSetting, user : String) {
    val configFile = File(getConfigPath(user))
    println("Attempting to Update Config File: " + configFile.toString())
    try {
        println("Converting Properties to Json")
        val json = Json {prettyPrint = true}
        val jsonString = json.encodeToString(userSetting)
        println("Writing Config")
        println(jsonString)
        val file = File(getConfigPath(user))
        val printWriter = PrintWriter(file) //Get where to write
        printWriter.write(jsonString)
        printWriter.close();
        println("Done Writing Config")
        updateConfig(user, file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
private fun getConfigPath(user : String): String? {
    val rootPath = Paths.get(System.getProperty("user.home"))
    val partialPath = Paths.get(".Markdown/config.txt")
    val resolvedPath: Path = rootPath.resolve(partialPath)
    return resolvedPath.toString()
}

public fun updateColorThemeConfig
            (userSetting: UserSetting, newTheme: String, user : String) : UserSetting {
    val newUserSetting =
        UserSetting(theme = newTheme,
            defaultFileLocation = userSetting.defaultFileLocation,
            defaultWidth = userSetting.defaultWidth,
            defaultHeight = userSetting.defaultHeight)
    updateConfig(newUserSetting, user)
    return newUserSetting
}
public fun updateFileLocationConfig
            (userSetting: UserSetting, newlocation: String, user : String) : UserSetting {
    val newUserSetting =
        UserSetting(theme = userSetting.theme,
            defaultFileLocation = newlocation,
            defaultWidth = userSetting.defaultWidth,
            defaultHeight = userSetting.defaultHeight)
    updateConfig(newUserSetting, user)
    return newUserSetting
}

public fun updateWidthConfig
            (userSetting: UserSetting, newWidth: Double, user : String) : UserSetting {
    val newUserSetting =
        UserSetting(theme = userSetting.theme,
            defaultFileLocation = userSetting.defaultFileLocation,
            defaultWidth = newWidth,
            defaultHeight = userSetting.defaultHeight)
    updateConfig(newUserSetting, user)
    return newUserSetting
}
public fun updateHeightConfig
            (userSetting: UserSetting, newHeight: Double, user : String) : UserSetting {
    val newUserSetting =
        UserSetting(theme = userSetting.theme,
            defaultFileLocation = userSetting.defaultFileLocation,
            defaultWidth = userSetting.defaultWidth,
            defaultHeight = newHeight)
    updateConfig(newUserSetting, user)
    return newUserSetting
}
