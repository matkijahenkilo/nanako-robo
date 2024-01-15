package org.matkija.bot.discordBot.abstracts

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent


abstract class SlashCommand {

    fun tryExecute(event: GenericCommandInteractionEvent, arg: String?) {
        try {
            execute(event, arg)
        } catch (e: Exception) {
            event.reply("```${e.message}```").setEphemeral(true).queue()
        }
    }

    protected abstract fun execute(event: GenericCommandInteractionEvent, arg: String?)

}