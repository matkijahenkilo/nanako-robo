# nanako-robo

A private use [JDA](https://github.com/discord-jda/JDA) bot written in Kotlin.

Her objective is to save gallery-dl's parseable links into a Postgres database and daily batch download media from those links as a solution to back up media.

Current `assets/config.json` file style:
```json
{
  "bots": [
    {
      "name" : "bot name",
      "token" : "token"
    }
  ],
  "server": {
      "ip": "a machine ip",
      "database": "postgres' database name",
      "user": "postgres' user",
      "password": "postgres' user's password"
  }
}
```