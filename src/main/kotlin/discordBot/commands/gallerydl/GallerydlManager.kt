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

    private fun filterAlreadyExistingLinks(newLinks: List<String>, dbLinks: List<String>): List<String> {

        val newValues = newLinks.toSet()
        val existingValues = dbLinks.toSet()

        if (existingValues.isEmpty()) {
            val ret = buildSet {
                newValues.forEach {
                    if (!it.contains("https://"))
                        return@forEach
                    add(it)
                }
            }
            return ret.toMutableList()
        }

        val ret = buildSet {
            newValues.forEach {
                if (it in existingValues || !it.contains("https://"))
                    return@forEach
                add(it)
            }
        }

        return ret.toList()
    }

    // arg can be either Long or String
    private fun isValid(str: String): Boolean =
        !(str.toLongOrNull() == null && !str.contains("https://"))

    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        val arg = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        event.deferReply().queue()

        when (event.subcommandName) {

            SlashCommandHelper.GALLERY_DL_SAVE -> {

                if (!isValid(arg!!)) {
                    event.hook.editMessage(content = "Link is not valid!").queue()
                    return
                }

                val args = filterAlreadyExistingLinks(arg.split(' '), databaseHandler.selectLink())
                if (args.isEmpty()) {
                    event.hook.editMessage(content = "Every link you tried to save was already saved.").queue()
                    return
                }
                args.forEach {
                    save(it, databaseHandler)
                }

                event.hook.editMessage(content = "Added:\n$args").queue()

            }

            SlashCommandHelper.GALLERY_DL_REMOVE -> {

                if (!isValid(arg!!)) {
                    event.hook.editMessage(content = "Value is not valid!").queue()
                    return
                }
                val removedLink = databaseHandler.readData(DatabaseAttributes.SELECT_WHERE_ID.format(arg))[0]
                remove(arg, databaseHandler)
                event.hook.editMessage(content = "Removed:\n<${removedLink.link}>").queue()

            }

            SlashCommandHelper.GALLERY_DL_RUN_MANUAL_DOWNLOAD -> {

                if (!isValid(arg!!)) {
                    event.hook.editMessage(content = "Link is not valid!").queue()
                    return
                }

                val args = arg.split(' ')

                if (args.isEmpty()) {
                    event.hook.editMessage(content = "Could not get anything from string.").queue()
                    return
                }

                args.forEach {
                    Gallerydl().download(it)
                }

                event.hook.editMessage(content = "Downloaded files from $args\nNew downloaded files shown in the terminal.")
                    .queue()

            }

            SlashCommandHelper.GALLERY_DL_LIST -> { // TODO: show list with buttons on discord

                databaseHandler.readData(DatabaseAttributes.SELECT).forEach {
                    println(it)
                }

            }

            SlashCommandHelper.GALLERY_DL_RUN_AUTO_DOWNLOADER -> {

                event.hook.editMessage(content = "Started scheduled downloader manually...").queue()

                runBlocking {
                    launch {
                        Gallerydl().downloadFromList(databaseHandler.selectAll())
                    }
                }

                event.hook.editMessage(content = "Finished downloading media manually.").queue()

            }

            else -> event.hook.editMessage(content = "I didn't do shit!").queue()

        }

    }

}