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

    fun download(): String {
        val command = "%s %s".format(GALLERYDL, link)
        val output: List<String>
        var ret = ""

        try {
            output = command.run(WORKINGDIR)!!.split("\n")
        } catch (e: Exception) {
            e.printStackTrace()
            return e.toString()
        }

        output.forEach {line ->
            if (!line.contains("# ")) {
                ret = ret.plus("$line\n")
            }
        }

        return ret
    }

    companion object {
        private const val GALLERYDL = "gallery-dl"
        private val WORKINGDIR = File("assets/")
    }

}