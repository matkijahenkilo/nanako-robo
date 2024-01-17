package org.matkija.bot.galleryDL

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.matkija.bot.sql.LinksTable
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class Gallerydl {

    private fun String.run(workingDir: File) {
        try {
            var line: String?
            val parts = this.split("\\s".toRegex())
            val child = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .start()

            val reader = BufferedReader(InputStreamReader(child.inputStream))

            while ((reader.readLine().also { line = it }) != null) {
                if (!line!!.contains("# "))
                    println(line)
            }

            child.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun download(link: String) {
        val command = "%s %s".format(GALLERYDL, link)
        command.run(WORKINGDIR)
    }

    suspend fun downloadFromList(links: List<LinksTable>) {
        coroutineScope {
            links.forEach {
                println("Downloading media from ${it.link}...")
                launch {
                    download(it.link)
                    println("downloaded from ${it.link}")
                }
            }
        }
    }

    companion object {
        private const val GALLERYDL = "gallery-dl"
        private val WORKINGDIR = File("assets/")
    }

}