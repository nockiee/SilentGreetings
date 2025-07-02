# SilentiumGreetings - Minecraft Social Commands Plugin

A lightweight Minecraft plugin that adds fun social interaction commands for players.

## Features

- **Greet players** (`/hi`, `/q`, `/greeting`) - Send friendly greetings
- **Hug players** (`/hug`) - Show affection with cute hug messages
- **Pay respects** (`/f`) - Honor recently deceased players

## Commands

| Command | Aliases | Description | Cooldown |
|---------|---------|-------------|----------|
| `/hi <player>` | `/q`, `/greeting`, `/hi`, `/hello`  | Greet another player | 5 seconds (editable) |
| `/hug <player>` | `/neck`, `/hg` | Hug another player | 10 seconds (editable) |
| `/f` | `/pressf`, `/respect` | Pay respects | - |

## Building

To build the plugin from source:

1. Make sure you have JDK and Maven installed
2. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/SilentiumGreetings.git```

3. Build with Maven:
    ```sh
    cd SilentiumGreetings
    mvn clean package
    ```

## Installation

1. Place the JAR file in your server's plugins folder

2. Restart your server

3. (Optional) Edit the generated config file in plugins/SilentiumGreetings/config.yml