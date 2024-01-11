package org.matkija.bot.discordBot.helper

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.Commands

object SlashCommandHelper {

    fun updateCommands(jda: JDA) {
        val commands = jda.updateCommands()

        commands.addCommands(
            Commands.slash("ping", "POing poing")
        )

        commands.queue()
    }

}