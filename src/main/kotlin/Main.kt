package org.matkija.bot

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.events.onCommandAutocomplete
import dev.minn.jda.ktx.jdabuilder.light
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.matkija.bot.discordBot.commands.gallerydl.GallerydlManager
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.galleryDL.AutoDownloader
import org.matkija.bot.sql.DatabaseHandler
import java.io.File
import kotlin.system.exitProcess

@Serializable
data class Bot(val name: String, val token: String)

private fun getBotsConfig(): List<Bot>? {
    try {
        return Json.decodeFromString<List<Bot>>(File("data/config.json").readText())
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun main() {

    val bots = getBotsConfig()

    if (bots!!.equals(null)) {
        exitProcess(2)
    }

    val bot = bots[0]
    val dbName = "${bot.name}.db"

    println("Connecting to database $dbName...")
    val databaseHandler = DatabaseHandler(dbName)

    println("Connecting to discord as an application...")
    println("Logging in as ${bot.name}")
    val jda = light(bot.token, enableCoroutines = true)

    SlashCommandHelper.updateCommands(jda)

    jda.onCommand(SlashCommandHelper.GALLERY_DL) { event ->
        GallerydlManager().tryExecute(event, databaseHandler)
    }

    jda.onCommandAutocomplete(SlashCommandHelper.GALLERY_DL) { event ->
        val options = SlashCommandHelper.getLinks(databaseHandler)

        event.replyChoices(options.choices.filter {
            it.name.contains(event.focusedOption.value)
        }).queue()
    }

    AutoDownloader().start(databaseHandler, 86400) // 24 hours in seconds
}