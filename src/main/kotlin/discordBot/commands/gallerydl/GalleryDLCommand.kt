package org.matkija.bot.discordBot.commands.gallerydl

import dev.minn.jda.ktx.messages.editMessage
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.discordBot.abstracts.SlashCommand
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.sql.DatabaseHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/* script
drop table links;

create table links (
	id 		  serial primary key,
	link	  varchar(255) not null,
	artist    varchar(255),
	dateAdded date
);

insert into links(link, artist, dateAdded) VALUES(
	'a nice link',
	'a nice person',
	'a nice date'
);

select * from links;
 */

class GalleryDLCommand : SlashCommand() {

    private fun save(link: String, databaseHandler: DatabaseHandler) {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.now().format(formatter)

        databaseHandler.saveData(INSERT.format(link, null, date)) //TODO Find a way to index author's account name

    }

    private fun remove(link: String) {
        //TODO postgres stuff
    }

    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        val link = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        if (link.isNullOrEmpty()) {
            event.reply("Value is empty!").queue()
            return
        }

        event.deferReply().queue()

        when (event.subcommandName) {
            SlashCommandHelper.GALLERY_DL_SAVE   -> {
                save(link, databaseHandler)
                event.hook.editMessage(content = "Added <$link>!").queue()
            }
            SlashCommandHelper.GALLERY_DL_REMOVE -> {
                remove(link)
                event.hook.editMessage(content = "Removed <$link>!").queue()
            }
            SlashCommandHelper.GALLERY_DL_LIST -> {
                databaseHandler.readData(SELECT.format(LINK)).forEach {
                    println(it)
                }
            }
            else -> event.hook.editMessage(content = "I didn't do shit!").queue()
        }

    }

    companion object {
        const val TABLE_NAME = "links"

        //attributes
        const val ID = "id"
        const val LINK = "link"
        const val ARTIST = "artist"
        const val DATE_ADDED = "dateAdded"

        //sql
        const val SELECT = "SELECT %s FROM $TABLE_NAME"
        const val INSERT = "INSERT INTO $TABLE_NAME($LINK, $ARTIST, $DATE_ADDED) VALUES('%s', '%s', '%s')"
    }

}