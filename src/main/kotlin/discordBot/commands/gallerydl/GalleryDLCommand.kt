package org.matkija.bot.discordBot.commands.gallerydl

import dev.minn.jda.ktx.messages.editMessage
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.discordBot.abstracts.SlashCommand
import org.matkija.bot.discordBot.helper.SlashCommandHelper
import org.matkija.bot.sql.DatabaseHandler

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

    private fun save(link: String) {
        //TODO postgres stuff
    }

    private fun remove(link: String) {
        //TODO postgres stuff
    }

    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        val arg = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        if (arg.isNullOrEmpty()) {
            event.reply("Value is empty!").queue()
            return
        }

        event.deferReply().queue()

        val option = event.subcommandName

        databaseHandler.readData(SELECT.format("*"))

        when (option) {
            SlashCommandHelper.GALLERY_DL_SAVE   -> {
                save(arg)
                event.hook.editMessage(content = "Added <$arg>!").queue()
            }
            SlashCommandHelper.GALLERY_DL_REMOVE -> {
                remove(arg)
                event.hook.editMessage(content = "Removed <$arg>!").queue()
            }
            else -> event.hook.editMessage(content = "I didn't do shit!").queue()
        }
    }

    companion object {
        const val ID = "id"
        const val LINK = "link"
        const val ARTIST = "artist"
        const val DATE_ADDED = "dateAdded"
        const val SELECT = "SELECT %s FROM links"
    }

}