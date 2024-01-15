package org.matkija.bot

import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.components.primary
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.messages.editMessage
import dev.minn.jda.ktx.messages.into
import dev.minn.jda.ktx.messages.reply_
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.entities.emoji.Emoji
import org.matkija.bot.discordBot.abstracts.SlashCommand
import org.matkija.bot.discordBot.commands.gallerydl.GalleryDLCommand
import org.matkija.bot.discordBot.commands.ping.Ping
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import java.io.File
import kotlin.time.Duration.Companion.seconds

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

    val jda = default(token, enableCoroutines = true)

    SlashCommandHelper.updateCommands(jda)

    val commands = HashMap<String, SlashCommand>()
    commands["ping"] = Ping()
    commands[SlashCommandHelper.GALLERY_DL] = GalleryDLCommand()


    jda.onCommand("ping") { event ->
        val confirm = primary("lol:ping", emoji = Emoji.fromUnicode("🎈"))
        event.reply_(
            "pongu?",
            components = confirm.into(),
            ephemeral = true
        ).queue()

        withTimeoutOrNull(5.seconds) { // 1 minute scoped timeout
            event.hook.editMessage(
                content = "All done~",
                components = emptyList()
            ).queue()
        } ?: event.hook.editMessage(/*id="@original" is default */content="Timed out.", components=emptyList()).queue()
    }

    jda.onCommand(SlashCommandHelper.GALLERY_DL) { event ->
        val value = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString
        commands[SlashCommandHelper.GALLERY_DL]?.tryExecute(event, value)
    }
}