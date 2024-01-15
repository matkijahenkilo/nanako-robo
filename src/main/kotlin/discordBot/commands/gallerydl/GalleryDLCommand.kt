package org.matkija.bot.discordBot.commands.gallerydl

import dev.minn.jda.ktx.messages.editMessage
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.discordBot.abstracts.SlashCommand
import org.matkija.bot.discordBot.helper.SlashCommandHelper

class GalleryDLCommand : SlashCommand() {

    private fun save(link: String) {
        //TODO postgres stuff
    }

    private fun remove(link: String) {
        //TODO postgres stuff
    }

    override fun execute(event: GenericCommandInteractionEvent, arg: String?) {

        if (arg.isNullOrEmpty()) {
            event.reply("Value is empty!").queue()
            return
        }

        event.deferReply().queue()

        val option = event.subcommandName

        when (option) {
            SlashCommandHelper.GALLERY_DL_SAVE   -> save(arg)
            SlashCommandHelper.GALLERY_DL_REMOVE -> remove(arg)
        }

        event.hook.editMessage(content = "Added <$arg>!").queue()
    }

}