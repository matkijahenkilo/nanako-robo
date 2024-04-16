# nanako-robo

A [JDA](https://github.com/discord-jda/JDA) bot for data hoarders.

Her objective is to save [gallery-dl](https://github.com/mikf/gallery-dl)'s parseable links and periodically download everything from those links as a solution to back up media.

### Installation

1. Download the zip file from releases and export it in a folder
2. Download [gallery-dl](https://github.com/mikf/gallery-dl/releases), place the .exe file at the same place as the .jar file and configure it if needed
3. Create `config.json` inside `data/` and copy and paste the following json and replace the values with your bot's information, token, downloader's delay, period and download path:
    
    Current `data/config.json` file style:
    
    ```json
    [
      {
        "bot" : {
          "name" : "bot's name",
          "token" : "token"
        },
        "timer" : {
          "initialDelay" : 3,
          "period" : 86400
        },
        "path" : "D:/gallery-dl"
      }
    ]
    ```
    
   *With the current `timer` settings, it will start downloading stuff 3 seconds after the bot has started, and it will run the same download function again after 86400 seconds (24 hours).*

4. Run `start.bat` or `java -jar nanako.jar` in a terminal and have fun

### Usage

Use slash commands in the bot's DMs or in a server that it is on.