package org.matkija.bot

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.events.onCommandAutocomplete
import dev.minn.jda.ktx.jdabuilder.default
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.matkija.bot.discordBot.commands.gallerydl.GallerydlManager
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.galleryDL.AutoDownloader
import org.matkija.bot.sql.DatabaseHandler
import java.io.File
import kotlin.system.exitProcess

@Serializable
data class Server(val ip: String, val database: String, val user: String, val password: String)

@Serializable
data class Bot(val name: String, val token: String)

@Serializable
data class Config(val bots: List<Bot>, val server: Server)

private fun getConfig(): Config? {
    val path = "data/config.json"
    try {
        return Json.decodeFromString<Config>(File(path).readText())
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun main() {

    val config = getConfig()

    if (config!!.equals(null)) {
        exitProcess(2)
    }

    println("Connecting to database...")
    val databaseHandler = DatabaseHandler(config.server)
    println("Connecting to discord as an application...")
    val jda = default(config.bots[0].token, enableCoroutines = true)

    SlashCommandHelper.updateCommands(jda)

    jda.onCommand(SlashCommandHelper.GALLERY_DL) { event ->
        GallerydlManager().tryExecute(event, databaseHandler)
        SlashCommandHelper.updateCommands(jda)
    }

    jda.onCommandAutocomplete(SlashCommandHelper.GALLERY_DL) { event ->
        val options = SlashCommandHelper.getLinks(databaseHandler)

        event.replyChoices(options.choices.filter {
            it.name.contains(event.focusedOption.value)
        }).queue()
    }

    AutoDownloader().start(databaseHandler, 86400) // 24 hours in seconds

}