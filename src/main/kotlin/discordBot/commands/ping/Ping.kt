package org.matkija.bot.discordBot.commands.ping

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.matkija.bot.discordBot.abstracts.SlashCommand


class Ping : SlashCommand() {

    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply("No pong for you.").queue()
    }

}