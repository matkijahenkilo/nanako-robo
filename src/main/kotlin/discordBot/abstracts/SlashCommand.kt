package org.matkija.bot.discordBot.abstracts

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent


abstract class SlashCommand {

    fun tryExecute(event: SlashCommandInteractionEvent) {
        try {
            execute(event)
        } catch (e: Exception) {
            event.reply("```${e.message}```").setEphemeral(true).queue()
        }
    }

    protected abstract fun execute(event: SlashCommandInteractionEvent)

}