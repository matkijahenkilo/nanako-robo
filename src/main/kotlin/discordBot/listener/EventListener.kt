package org.matkija.bot.discordBot.listener

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.matkija.bot.discordBot.abstracts.SlashCommand
import org.matkija.bot.discordBot.commands.ping.Ping


class EventListener : ListenerAdapter() {

    private val commands = HashMap<String, SlashCommand>()

    init {

        commands["ping"] = Ping()

    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {

        val name = event.name

        try {
            commands[name]?.tryExecute(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}