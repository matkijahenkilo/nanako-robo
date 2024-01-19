package org.matkija.bot.abstracts

import dev.minn.jda.ktx.messages.editMessage
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.sql.DatabaseHandler


abstract class SlashCommand {

    fun tryExecute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler) {
        try {
            execute(event, databaseHandler)
        } catch (e: Exception) {
            e.printStackTrace()
            if (event.isAcknowledged) {
                event.hook.editMessage(content = "```${e.message}```").queue()
            } else {
                event.reply("```${e.message}```").setEphemeral(true).queue()
            }
        }
    }

    protected abstract fun execute(event: GenericCommandInteractionEvent, databaseHandler: DatabaseHandler)

}