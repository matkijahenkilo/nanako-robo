package org.matkija.bot.galleryDL

import org.matkija.bot.discordBot.helper.DatabaseAttributes
import org.matkija.bot.sql.DatabaseHandler
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class AutoDownloader {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    fun startDailyDownload(databaseHandler: DatabaseHandler) {
        val beeper = Runnable {
            println("Downloading links from database...")
            val links = databaseHandler.readData(DatabaseAttributes.SELECT)

            links.forEach {link ->
                println("Downloading media from ${link.link}")
                GalleryDL(link.link).download()
            }

        }

        scheduler.scheduleAtFixedRate(beeper, 1, 86400, TimeUnit.SECONDS)
    }
}