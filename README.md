# SilentiumGreetings - Minecraft Social Commands Plugin

SilentiumGreetings is a lightweight and customizable Minecraft plugin that adds friendly social commands to your server: greetings, hugs, high-fives, goodbyes, and paying respects to the recently deceased. Perfect for roleplay, community, or just making your server a warmer place!

---

## Features

- **Greetings**: Say hello to another player with `/hi <player>` (or `/q`, `/greeting`, `/hello`)
- **Hugs**: Hug a nearby player with `/hug` (or `/neck`, `/hg`)
- **High Five**: Give a high five to a player within 5 blocks using `/five` (or `/givefive`, `/highfive`)
- **Goodbye**: Say goodbye to a player with `/bye <player>` (or `/goodbye`, `/bb`)
- **Pay Respects**: Pay respects to the last deceased player with `/f` (or `/pressf`, `/respect`)
- **Customizable Messages**: All messages, colors, and radii are configurable via `config.yml`
- **Cooldowns**: Configurable cooldowns for commands
- **HEX Color Support**: Messages support beautiful HEX colors (Minecraft 1.16+)
- **Local & Global Messages**: Hugs and high-fives are local (radius), other commands are global

---

## Commands

| Command           | Aliases                        | Description                                   | Cooldown (default) |
|-------------------|-------------------------------|-----------------------------------------------|--------------------|
| `/hi <player>`    | `/q`, `/greeting`, `/hello`   | Greet another player                          | 5 seconds          |
| `/hug`            | `/neck`, `/hg`                | Hug a nearby player                           | 10 seconds         |
| `/five`           | `/givefive`, `/highfive`      | High five a player within 5 blocks            | 10 seconds         |
| `/bye <player>`   | `/goodbye`, `/bb`             | Say goodbye to another player                 | -                  |
| `/f`              | `/pressf`, `/respect`         | Pay respects to the last deceased player      | -                  |

---

## Installation

1. Download the latest SilentiumGreetings.jar from [releases](https://github.com/nockiee/SilentiumGreetings/releases/)
2. Place the file in your server's `plugins` folder
3. Restart your server
4. (Optional) Edit `plugins/SilentiumGreetings/config.yml` to your liking

---

## Configuration

- **Messages**: All command texts, colors, and message variations are in `config.yml`
- **HEX Colors**: Use `<#RRGGBB>` format for beautiful colors (e.g. `<#7ed957>`)
- **Radii**: Message visibility radius for hugs and high-fives is set in `settings.ranges`
- **Cooldowns**: Command cooldowns are set in `settings.timeouts`

---

## Example config.yml

```yaml
settings:
  ranges:
    hug: 3.0
    hug-message: 50.0
    greet-message: 50.0
    five: 5.0
    five-message: 50.0

  timeouts:
    respect: 300
    cooldown-hug: 10
    cooldown-greet: 5
    cooldown-five: 10

messages:
  greetings:
    - "<#7ed957>{player1} <#e0e0e0>greets <#7ec8e3>{player2}"
  hugs:
    - "<#f7b2ad>🤗 <#7ed957>{player1} <#e0e0e0>hugs <#7ec8e3>{player2} <#f7b2ad>🤗"
  fives:
    - "<#7ed957>{player1} <#e0e0e0>gives a high five to <#7ec8e3>{player2} ✋"
  byes:
    - "<#e0e0e0>{player1} <#f7b2ad>says goodbye to <#7ec8e3>{player2}"
  respects:
    - "<#bdbdbd>⚰ <#e0e0e0>{player} paid respects to <#bdbdbd>{dead_player} <#bdbdbd>⚰"
  errors:
    no-target: "<#f08080>Player not found! Get closer (max {distance} blocks)"
    player-offline: "<#f08080>Player is offline!"
    cooldown: "<#ffe066>Please wait {time} seconds."
    no-recent-death: "<#bdbdbd>No recent deaths"
    usage: "<#ffe066>Usage: <#7ed957>/hi <player> <#bdbdbd>or <#7ed957>/q <player>"
    self-action: "<#f08080>You can't use this action on yourself"
```

---

## For Players

- Use the commands to interact and have fun with others!
- HEX colors will display beautifully in chat (Minecraft 1.16+)
- If a command doesn't work, check the cooldown or required distance

---

## For Developers

- Written in Java using the PaperMC API
- Each command is implemented as a separate handler class
- All customization is done via `config.yml` (no need to recompile)
- To build from source:
    ```sh
    mvn clean package
    ```
- To add new commands or messages, extend the config and add new handlers

---

## License

MIT License. Use, modify, and share freely!

---

**SilentiumGreetings** — make your server warmer and friendlier!