package org.matkija.bot.galleryDL

import org.matkija.bot.discordBot.helper.DatabaseAttributes
import org.matkija.bot.sql.DatabaseHandler
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class AutoDownloader {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    fun start(databaseHandler: DatabaseHandler, period: Long) {
        val downloader = Runnable {
            println("Fetching links from database...")
            val links = databaseHandler.readData(DatabaseAttributes.SELECT)
            println("Downloading links from database...")

            links.forEach { link ->
                println("Downloading media from ${link.link}")
                println(GalleryDL(link.link).download())
            }

            println("Finished downloading everything!")
        }

        scheduler.scheduleAtFixedRate(downloader, 1, period, TimeUnit.SECONDS)
    }
}