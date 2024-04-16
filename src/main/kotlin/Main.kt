package org.matkija.bot

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.events.onCommandAutocomplete
import dev.minn.jda.ktx.jdabuilder.light
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.matkija.bot.discordBot.commands.gallerydl.GallerydlManager
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.galleryDL.startScheduledTimer
import org.matkija.bot.sql.DatabaseHandler
import java.io.File
import kotlin.system.exitProcess

@Serializable
data class Bot(val name: String, val token: String)

@Serializable
data class Timer(val initialDelay: Long, val period: Long)

@Serializable
data class Config(val bot: Bot, val timer: Timer, val path: String)

private fun getBotsConfigs(): List<Config>? {
    try {
        return Json.decodeFromString<List<Config>>(File("data/config.json").readText())
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun main() {

    val configs = getBotsConfigs()

    if (configs!!.equals(null)) {
        exitProcess(2)
    }

    val config = configs[0]
    val bot = config.bot
    val dbName = "${bot.name}.db"

    println("Connecting to database $dbName...")
    val databaseHandler = DatabaseHandler(dbName)

    println("Connecting to discord as an application...")
    println("Logging in as ${bot.name}")
    val jda = light(bot.token, enableCoroutines = true)
    jda.awaitReady()

    SlashCommandHelper.updateCommands(jda)

    jda.onCommand(SlashCommandHelper.GALLERY_DL) { event ->
        GallerydlManager(config).tryExecute(event, databaseHandler)
    }

    jda.onCommandAutocomplete(SlashCommandHelper.GALLERY_DL) { event ->
        val options = SlashCommandHelper.getLinks(databaseHandler)

        event.replyChoices(options.choices.filter {
            it.name.contains(event.focusedOption.value)
        }).queue()
    }

    startScheduledTimer(databaseHandler, config)
}