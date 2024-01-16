package org.matkija.bot.discordBot.abstracts

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.sql.DatabaseHandler


abstract class SlashCommand {

    fun tryExecute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {
        try {
            execute(event, databaseHandler)
        } catch (e: Exception) {
            event.reply("```${e.message}```").setEphemeral(true).queue()
        }
    }

    protected abstract fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler)

}