# SilentGreetings - Minecraft Social Commands Plugin

SilentGreetings is a lightweight and highly customizable PaperMC plugin that adds friendly social commands to your server: greetings, hugs, high-fives, goodbyes, and paying respects. Perfect for roleplay, community, or just making your server a warmer place!

---

## ✨ What's New

- **Global messages:** `/hi` and `/bye` without arguments now broadcast a "greeted everyone" or "said goodbye to everyone" message (fully configurable)
- **Configurable aliases:** Command aliases are now set in `config.yml`
- **Improved HEX color support** for all messages
- **Full message localization** via config

---

## 📦 Features

- **Greetings:** `/hi <player>` (or aliases)
- **Hugs:** `/hug` (or aliases)
- **High Five:** `/five` (or aliases)
- **Goodbye:** `/bye <player>` (or aliases)
- **Pay Respects:** `/f` (or aliases)
- **Global messages:** `/hi` or `/bye` without arguments greets or says goodbye to everyone
- **Fully configurable:** All messages, colors, radii, and aliases in `config.yml`
- **Cooldowns:** Configurable cooldowns for commands
- **HEX color support:** Beautiful chat colors (Minecraft 1.16+)
- **Local & global messages:** Hugs and high-fives are local (radius), others are global

---

## 📝 Commands & Aliases

| Command           | Aliases (configurable)         | Description                                   | Cooldown (default)   |
|-------------------|-------------------------------|-----------------------------------------------|----------------------|
| `/hi <player>`    | `/q`, `/greeting`, `/hello`   | Greet a player or everyone                    | 5 seconds            |
| `/hug`            | `/neck`, `/hg`                | Hug a nearby player                           | 10 seconds           |
| `/five`           | `/givefive`, `/highfive`      | High five a player within 5 blocks            | 10 seconds           |
| `/bye <player>`   | `/goodbye`, `/bb`             | Say goodbye to a player or everyone           | -                    |
| `/f`              | `/pressf`, `/respect`         | Pay respects to the last deceased player      | -                    |

**Aliases are now fully configurable in `config.yml`!**

---

## ⚙️ Installation

1. Download the latest SilentGreetings.jar from [releases](https://github.com/nockiee/SilentGreetings/releases/)
2. Place the file in your server's `plugins` folder
3. Restart your server
4. (Optional) Edit `plugins/SilentGreetings/config.yml` to your liking

---

## 🛠️ Configuration

- **Messages:** All texts and colors are in the `messages` section of `config.yml`
- **HEX Colors:** Use `<#RRGGBB>` for beautiful colors (e.g. `<#7ed957>`)
- **Radii:** Visibility radius for hugs and high-fives is in `settings.ranges`
- **Cooldowns:** Command cooldowns are in `settings.timeouts`
- **Aliases:** All command aliases are in the `aliases` section

---

## 📄 Example config.yml

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

aliases:
  hi: [q, greeting, hello]
  bye: [goodbye, bb]
  hug: [neck, hg]
  five: [givefive, highfive]
  f: [pressf, respect]

messages:
  greetings:
    - "<#7ed957>{player1} <#e0e0e0>greets <#7ec8e3>{player2}"
  greet-all: "<#7ed957>{player1} <#e0e0e0>greeted everyone!"
  hugs:
    - "<#f7b2ad>🤗 <#7ed957>{player1} <#e0e0e0>hugged <#7ec8e3>{player2} <#f7b2ad>🤗"
  fives:
    - "<#7ed957>{player1} <#e0e0e0>high-fived <#7ec8e3>{player2} ✋"
  byes:
    - "<#e0e0e0>{player1} <#f7b2ad>said goodbye to <#7ec8e3>{player2}"
  bye-all: "<#e0e0e0>{player1} <#f7b2ad>said goodbye to everyone!"
  respects:
    - "<#bdbdbd>⚰ <#e0e0e0>{player} paid respects to <#bdbdbd>{dead_player} <#bdbdbd>⚰"
  errors:
    no-target: "<#f08080>Player not found! Get closer (max {distance} blocks)"
    player-offline: "<#f08080>Player is offline!"
    cooldown: "<#ffe066>Please wait {time} seconds."
    no-recent-death: "<#bdbdbd>No recent deaths"
    usage: "<#ffe066>Usage: <#7ed957>/hi <player> <#bdbdbd>or <#7ed957>/q <player>"
    self-action: "<#f08080>You can't use this command on yourself"
```

---

## 👤 For Players

- Use the commands to interact and have fun with others!
- HEX colors will display beautifully in chat (Minecraft 1.16+)
- If a command doesn't work, check the cooldown or required distance

---

## 👨‍💻 For Developers

- Written in Java using the PaperMC API
- Each command is implemented as a separate handler class
- All customization is done via `config.yml` (no need to recompile)
- To build from source:
    ```sh
    mvn clean package
    ```
- To add new commands or messages, extend the config and add new handlers

---

**SilentGreetings** — make your server warmer and friendlier!