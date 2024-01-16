package org.matkija.bot.discordBot.commands.gallerydl

import dev.minn.jda.ktx.messages.editMessage
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.abstracts.SlashCommand
import org.matkija.bot.discordBot.helper.DatabaseAttributes
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

        //TODO Find a way to index author's account name
        databaseHandler.prepareStatement(DatabaseAttributes.INSERT.format(link, null, date))

    }

    private fun remove(link: String, databaseHandler: DatabaseHandler) {

        databaseHandler.prepareStatement(DatabaseAttributes.DELETE.format(link))

    }

    override fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {

        val arg = event.getOption(SlashCommandHelper.GALLERY_DL_LINK)?.asString

        if (arg.isNullOrEmpty()) {
            event.reply("Value is empty!").queue()
            return
        }

        if (!arg.contains("https://")) {
            event.reply("Link is not valid!").queue()
            return
        }

        event.deferReply().queue()

        when (event.subcommandName) {

            SlashCommandHelper.GALLERY_DL_SAVE   -> {

                save(arg, databaseHandler)
                event.hook.editMessage(content = "Added <$arg>!").queue()

            }

            SlashCommandHelper.GALLERY_DL_REMOVE -> {

                val removedLink = databaseHandler.readData(DatabaseAttributes.SELECT_WHERE_ID.format(arg))[0]
                remove(arg, databaseHandler)
                event.hook.editMessage(content = "Removed <${removedLink.link}>!").queue()

            }

            SlashCommandHelper.GALLERY_DL_LIST -> {

                databaseHandler.readData(DatabaseAttributes.SELECT).forEach {
                    println(it)
                }

            }

            else -> event.hook.editMessage(content = "I didn't do shit!").queue()

        }

    }

}