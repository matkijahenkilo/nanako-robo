package org.matkija.bot.discordBot.commands.ping

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.matkija.bot.discordBot.abstracts.SlashCommand


class Ping : SlashCommand() {

    override fun execute(event: GenericCommandInteractionEvent, arg: String?) {
        event.reply("No pong for you.").queue()
    }

}