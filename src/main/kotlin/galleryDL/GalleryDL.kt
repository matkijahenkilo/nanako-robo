package org.matkija.bot.galleryDL

import java.io.File
import java.io.IOException

class GalleryDL(
    private val link: String,
) {

    private fun String.run(workingDir: File): String? {
        try {
            val parts = this.split("\\s".toRegex())
            val child = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            child.waitFor()
            return child.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun download(): String? {
        val command = "%s %s".format(GALLERYDL, link)
        return command.run(WORKINGDIR)
    }

    companion object {
        private const val GALLERYDL = "gallery-dl"
        private val WORKINGDIR = File("assets/")
    }

}