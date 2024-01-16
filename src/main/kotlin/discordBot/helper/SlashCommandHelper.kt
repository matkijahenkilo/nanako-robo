package org.matkija.bot.discordBot.helper

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.matkija.bot.sql.DatabaseHandler


object SlashCommandHelper {

    const val GALLERY_DL = "gallery-dl"
    const val GALLERY_DL_LINK = "link"
    const val GALLERY_DL_SAVE = "save"
    const val GALLERY_DL_REMOVE = "remove"
    const val GALLERY_DL_LIST = "list"

    fun updateCommands(jda: JDA) {
        val commands = jda.updateCommands()

        commands.addCommands(

            Commands.slash(GALLERY_DL, "Manages my gallery-dl list").addSubcommands(
                SubcommandData(GALLERY_DL_SAVE, "Saves a link to back up it's contents everyday")
                    .addOption(OptionType.STRING, GALLERY_DL_LINK, "A link that gallery-dl can parse", true),
                SubcommandData(GALLERY_DL_REMOVE, "Removes a link from my database")
                    .addOption(OptionType.INTEGER, "link", "Link to remove", true, true),
                SubcommandData(GALLERY_DL_LIST, "Shows you a list of saved links")
            ),

        )

        commands.queue()
    }

    fun getLinks(databaseHandler: DatabaseHandler): OptionData {
        val optionData = OptionData(OptionType.INTEGER, "link", "Da!!! link")
        val links = databaseHandler.readData("SELECT * FROM ${DatabaseAttributes.TABLE_NAME}")

        links.forEach { link ->
            optionData.addChoice(link.link, link.id)
        }

        return optionData
    }

}