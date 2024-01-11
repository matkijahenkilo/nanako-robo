package org.matkija.bot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.discordBot.listener.EventListener
import java.io.File

private fun getToken(): String {
    val path = "data/config"
    try {
        return File(path).readText()
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

fun main() {
    val token = getToken()

    val jda = JDABuilder.createDefault(token)
        .enableIntents(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.MESSAGE_CONTENT,
        )
        .addEventListeners(EventListener())
        .build()

    SlashCommandHelper.updateCommands(jda)
}