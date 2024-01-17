package org.matkija.bot.discordBot.commands.gallerydl

import dev.minn.jda.ktx.messages.editMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.abstracts.SlashCommand
import org.matkija.bot.discordBot.helper.DatabaseAttributes
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.galleryDL.Gallerydl
import org.matkija.bot.sql.DatabaseHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class GallerydlManager : SlashCommand() {

    private fun save(link: String, databaseHandler: DatabaseHandler) {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.now().format(formatter)

        //TODO Find a way to index author's account name
        databaseHandler.runStatement(DatabaseAttributes.INSERT.format(link, null, date))

    }

    private fun remove(link: String, databaseHandler: DatabaseHandler) {

        databaseHandler.runStatement(DatabaseAttributes.DELETE.format(link))

    }

    private fun isAlreadySaved(link: String, databaseHandler: DatabaseHandler): Boolean {
        TODO("I'm lazy.")
    }

    // arg can be either Long or String
    private fun isValid(str: String): Boolean =
        !(str.toLongOrNull() == null && !str.contains("https://"))

    //TODO: make bot accept more than one link per command
    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        val arg = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        event.deferReply().queue()

        when (event.subcommandName) {

            SlashCommandHelper.GALLERY_DL_SAVE -> {
                if (isValid(arg!!)) {
                    event.reply("Link is not valid!").queue()
                    return
                }
                save(arg, databaseHandler)
                event.hook.editMessage(content = "Added:\n<$arg>").queue()

            }

            SlashCommandHelper.GALLERY_DL_REMOVE -> {
                if (!isValid(arg!!)) {
                    event.reply("Link is not valid!").queue()
                    return
                }
                val removedLink = databaseHandler.readData(DatabaseAttributes.SELECT_WHERE_ID.format(arg))[0]
                remove(arg, databaseHandler)
                event.hook.editMessage(content = "Removed:\n<${removedLink.link}>").queue()

            }

            SlashCommandHelper.GALLERY_DL_RUN_MANUAL_DOWNLOAD -> {
                if (!isValid(arg!!)) {
                    event.reply("Link is not valid!").queue()
                    return
                }
                Gallerydl().download(arg)
                event.hook.editMessage(content = "Downloaded files from <$arg>\nNew files shown in the terminal.")
                    .queue()

            }

            SlashCommandHelper.GALLERY_DL_LIST -> {

                databaseHandler.readData(DatabaseAttributes.SELECT).forEach {
                    println(it)
                }

            }

            SlashCommandHelper.GALLERY_DL_RUN_AUTO_DOWNLOADER -> {

                runBlocking {
                    launch {
                        Gallerydl().downloadFromList(databaseHandler.selectAll())
                    }
                }
                event.hook.editMessage(content = "Downloaded files from <$arg>").queue()

            }

            else -> event.hook.editMessage(content = "I didn't do shit!").queue()

        }

    }

}