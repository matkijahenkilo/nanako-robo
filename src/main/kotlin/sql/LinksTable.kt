package org.matkija.bot.sql

data class LinksTable(
    val id: Long,
    val link: String,
    val artist: String?,
    val dateAdded: String?
)
