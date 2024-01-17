# nanako-robo

A private [JDA](https://github.com/discord-jda/JDA) bot written in Kotlin.

Her objective is to save [gallery-dl](https://github.com/mikf/gallery-dl)'s parseable links into a Postgres database and daily batch download media from those links as a solution to back up media.

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

Postgres scripts:

```sql
DROP TABLE links;

CREATE TABLE links (
    id        SERIAL PRIMARY KEY,
    link      varchar(255) NOT NULL,
    artist    varchar(255),
    dateAdded date
);

INSERT INTO links (link, artist, dateAdded) VALUES (
    'a nice link',
    'a nice person',
    'a nice date'
);

SELECT * FROM links;
```
