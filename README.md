# nanako-robo

A [JDA](https://github.com/discord-jda/JDA) bot for data hoarders.

Her objective is to save [gallery-dl](https://github.com/mikf/gallery-dl)'s parseable links and periodically download everything from those links as a solution to back up media.

### Installation

1. Download the zip file from releases and export it in a folder
2. Download [gallery-dl](https://github.com/mikf/gallery-dl/releases), place the .exe file at the same place as the .jar file and configure it if needed
3. Create `config.json` inside `data/` and copy and paste the following json and replace the information with your bot's information:

Current `data/config.json` file style:

```json
[
    {
        "name" : "bot's name",
        "token" : "token"
    }
]
```

4. Run `start.bat` or `java -jar nanako.jar` in a terminal

Default download path is D:/gallery-dl, you can change it in `data/gallerydldir.json`
