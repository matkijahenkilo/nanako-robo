# nanako-robo

A private [JDA](https://github.com/discord-jda/JDA) bot written in Kotlin.

Her objective is to save [gallery-dl](https://github.com/mikf/gallery-dl)'s parseable links into a Postgres database and daily batch download media from those links as a solution to back up media.

Current `assets/config.json` file style:

```json
[
    {
        "name" : "bot name",
        "token" : "token"
    }
]
```