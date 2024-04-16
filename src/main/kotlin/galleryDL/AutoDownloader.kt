package org.matkija.bot.galleryDL

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.matkija.bot.Config
import org.matkija.bot.sql.DatabaseHandler
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

fun startScheduledTimer(databaseHandler: DatabaseHandler, config: Config) {
    val timer = config.timer

    val downloader = Runnable {
        println("Automatically fetching links from database...")
        runBlocking { launch { Gallerydl(config).downloadFromList(databaseHandler.selectAll()) } }
        println("Finished downloading everything!")
    }

    println("Downloading contents automatically in ${timer.initialDelay} seconds")

    scheduler.scheduleAtFixedRate(downloader, timer.initialDelay, timer.period, TimeUnit.SECONDS)

}
