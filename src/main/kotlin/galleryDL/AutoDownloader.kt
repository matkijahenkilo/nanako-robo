package org.matkija.bot.galleryDL

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.matkija.bot.sql.DatabaseHandler
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class AutoDownloader {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    fun start(databaseHandler: DatabaseHandler, period: Long, initialDelay: Long) {

        val downloader = Runnable {
            println("Automatically fetching links from database...")
            runBlocking { launch { Gallerydl().downloadFromList(databaseHandler.selectAll()) } }
            println("Finished downloading everything!")
        }

        println("Downloading contents automatically in $initialDelay seconds")

        scheduler.scheduleAtFixedRate(downloader, initialDelay, period, TimeUnit.SECONDS)

    }
}