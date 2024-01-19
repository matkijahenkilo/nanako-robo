package org.matkija.bot.galleryDL

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.matkija.bot.sql.DatabaseHandler
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class AutoDownloader {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    fun start(databaseHandler: DatabaseHandler, period: Long) {

        val downloader = Runnable {
            println("Automatically fetching links from database...")
            runBlocking { launch { Gallerydl().downloadFromList(databaseHandler.selectAll()) } }
            println("Finished downloading everything!")
        }

        val initialDelay: Long = 120

        // initialDelay.seconds prints 2m
        // while initialDelay.minutes prints 2h
        // ...confusing
        println("Downloading contents automatically in ${initialDelay.seconds}")

        scheduler.scheduleAtFixedRate(downloader, initialDelay, period, TimeUnit.SECONDS)

    }
}