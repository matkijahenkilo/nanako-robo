package org.matkija.bot.galleryDL

import kotlinx.coroutines.*
import org.matkija.bot.Config
import org.matkija.bot.sql.LinksTable
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class Gallerydl(config: Config) {

    private val arguments = "--cookies cookies.txt --ugoira-conv -d %s".format(config.path)

    private suspend fun String.run() {
        try {
            val workingDir = File(WORKING_DIR)
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
        val command = "%s %s %s".format(GALLERYDL, arguments, link)
        runBlocking { command.run() }
    }

    fun getUserName(link: String): String? {
        try {
            // haha well...
            val command = if (link.contains("https://www.pixiv.net")
                || link.contains("https://twitter.com")
                || link.contains("https://x.com")
            ) {

                "%s %s %s".format(GALLERYDL, PRINT_NAME, link)

            } else if (link.contains("https://baraag.net")
                || link.contains("https://pawoo.net")
            ) {

                "%s %s %s".format(GALLERYDL, PRINT_ACCOUNT_USERNAME, link)

            } else {
                "%s %s %s".format(GALLERYDL, PRINT_USERNAME, link)
            }
            val workingDir = File(WORKING_DIR)
            val parts = command.split("\\s".toRegex())
            val child = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            child.waitFor()

            var name = child.inputStream.bufferedReader().readText()
            name = name.replace("\n", "")
            name = name.replace("\r", "")

            return name
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
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
        private const val PRINT_USERNAME = "--filter \"print(user['username']) or abort()\" --range 0 -s"
        private const val PRINT_NAME = "--filter \"print(user['name']) or abort()\" --range 0 -s"
        private const val PRINT_ACCOUNT_USERNAME = "--filter \"print(account['username']) or abort()\" --range 0 -s"
        private const val WORKING_DIR = "./"
    }

}