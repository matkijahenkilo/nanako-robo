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
        val name = Gallerydl().getUserName(link)

        databaseHandler.runStatement(DatabaseAttributes.INSERT.format(link, name, date))

    }

    private fun remove(link: String, databaseHandler: DatabaseHandler) =
        databaseHandler.runStatement(DatabaseAttributes.DELETE.format(link))

    private fun removeEverything(databaseHandler: DatabaseHandler): List<String> {
        val list = databaseHandler.selectAllLinks()
        databaseHandler.createTable(true)
        return list
    }

    private fun filterAlreadyExistingLinks(newLinks: List<String>, dbLinks: List<String>): List<String> {

        val newValues = newLinks.toSet()
        val existingValues = dbLinks.toSet()

        if (existingValues.isEmpty()) {
            val ret = buildSet {
                newValues.forEach {
                    if (!it.contains("https://")) return@forEach
                    add(it)
                }
            }
            return ret.toList()
        }

        val ret = buildSet {
            newValues.forEach {
                if (it in existingValues || !it.contains("https://")) return@forEach
                add(it)
            }
        }

        return ret.toList()
    }

    // arg can be either Long or String
    private fun isValid(str: String): Boolean =
        !(str.toLongOrNull() == null && !str.contains("https://"))

    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        event.deferReply().queue()

        when (event.subcommandName) {
            SlashCommandHelper.GALLERY_DL_LIST -> {

                var str = ""

                databaseHandler.readData(DatabaseAttributes.SELECT).forEach {
                    str += "%s. <%s> (%s)\n".format(it.id, it.link, it.artist)
                }

                /* TODO: buttons lmao
                val previous = primary("gdl:previous", emoji = Emoji.fromUnicode("⬅"))
                val next = primary("gdl:next", emoji = Emoji.fromUnicode("➡"))
                val comps = row(previous, next).into()
                */

                event.hook.editMessage(
                    content = str
                ).queue()

                return
            }

            SlashCommandHelper.GALLERY_DL_RUN_AUTO_DOWNLOADER -> {

                event.hook.editMessage(content = "Started scheduled downloader manually...").queue()

                runBlocking {
                    launch {
                        Gallerydl().downloadFromList(databaseHandler.selectAll())
                    }
                }

                event.hook.editMessage(content = "Finished downloading media manually.").queue()
                return
            }
        }

        val arg = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        if (!isValid(arg!!)) {
            event.hook.editMessage(content = "Link is not valid!").queue()
            return
        }

        when (event.subcommandName) {

            SlashCommandHelper.GALLERY_DL_SAVE -> {

                val args = filterAlreadyExistingLinks(arg.split(' '), databaseHandler.selectAllLinks())
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

                if (arg.toInt() == -1) {
                    val links = removeEverything(databaseHandler)
                    var reply = "Removed entries:\n<$links>\nTable also got reset for convenience"
                    if (reply.length > 1990)
                        reply = reply.substring(1, 1990)
                    event.hook.editMessage(content = reply).queue()
                } else {
                    val removedLink = databaseHandler.readData(DatabaseAttributes.SELECT_WHERE_ID.format(arg))[0]
                    remove(arg, databaseHandler)
                    event.hook.editMessage(content = "Removed:\n<${removedLink.link}>").queue()
                }

            }

            SlashCommandHelper.GALLERY_DL_RUN_MANUAL_DOWNLOAD -> {

                val args = arg.split(' ')

                if (args.isEmpty()) {
                    event.hook.editMessage(content = "Could not get anything from string.").queue()
                    return
                }

                event.hook.editMessage(content = "Downloading media from $args...").queue()

                args.forEach {
                    Gallerydl().download(it)
                }

                event.hook.editMessage(content = "Downloaded media from $args\nNew downloaded files shown in the terminal.")
                    .queue()

            }

            else -> event.hook.editMessage(content = "I didn't do shit!").queue()

        }

    }

}