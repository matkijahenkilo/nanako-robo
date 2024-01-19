package org.matkija.bot.galleryDL

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.matkija.bot.sql.LinksTable
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

@Serializable
data class GallerydlDir(val path: String)

class Gallerydl {

    private val config = Json.decodeFromString<GallerydlDir>(File("data/gallerydldir.json").readText())

    private suspend fun String.run() {
        try {
            val workingDir = File(config.path)
            var line: String?
            val parts = this.split("\\s".toRegex())
            val child = withContext(Dispatchers.IO) {
                ProcessBuilder(*parts.toTypedArray())
                    .directory(workingDir)
                    .start()
            }

            val reader = BufferedReader(InputStreamReader(child.inputStream))

            while ((withContext(Dispatchers.IO) { reader.readLine() }.also { line = it }) != null) {
                if (!line!!.contains("# "))
                    println(line)
            }

            withContext(Dispatchers.IO) { child.waitFor() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun download(link: String) {
        val command = "%s %s".format(GALLERYDL, link)
        runBlocking { command.run() }
    }

    suspend fun downloadFromList(links: List<LinksTable>) {
        coroutineScope {
            links.forEach {
                launch {
                    println("Downloading media from ${it.link}...")
                    download(it.link)
                    println("Downloaded from ${it.link}")
                }
            }
        }
    }

    companion object {
        private const val GALLERYDL = "gallery-dl"
    }

}