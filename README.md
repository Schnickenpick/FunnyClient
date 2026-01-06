# FunnyClient 🎮

A hilarious Minecraft utility client for anarchy servers like 2b2t!

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen)
![Fabric](https://img.shields.io/badge/Fabric-0.16.5-orange)
![License](https://img.shields.io/badge/License-GPL--3.0-blue)

## Features ✨

### Combat Modules
- **Crystal Aura** - Automatically places and breaks end crystals with smart damage calculation
  - Auto-target nearest player
  - Min/max damage settings
  - Auto-switch to crystals
  - Rotation support

### Movement Modules
- **Flight** - Multiple flight modes for maximum mobility
  - Vanilla mode (standard fly)
  - Packet mode (bypass anti-cheat)
  - Creative mode (full creative flight)
  - Configurable speed and vertical speed

## Building 🛠️

### Prerequisites
- Java 21 or higher
- Gradle (included via wrapper)

### Build Steps
```bash
# Clone the repository
git clone https://github.com/Schnickenpick/FunnyClient.git
cd FunnyClient

# Build the mod
./gradlew build

# The compiled JAR will be in build/libs/
```

## Installation 📦

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.4
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for 1.21.4
3. Download the latest FunnyClient JAR from [Releases](https://github.com/Schnickenpick/FunnyClient/releases)
4. Place both JARs in your `.minecraft/mods` folder
5. Launch Minecraft with the Fabric profile!

## Usage 🎯

### Module Activation
- Modules can be toggled by binding keys in the settings
- Default keybinds will be added in future updates

### Module Settings
Each module has customizable settings:
- **Flight**: Mode (Vanilla/Packet/Creative), Speed, Vertical Speed
- **Crystal Aura**: Range, Place Range, Auto Switch, Rotate, Min Damage, Max Self Damage

## Development 💻

### Project Structure
```
FunnyClient/
├── src/main/java/com/funnyclient/
│   ├── FunnyClient.java          # Main mod class
│   ├── modules/                   # All modules
│   │   ├── Module.java           # Base module class
│   │   ├── ModuleManager.java    # Module registry
│   │   ├── combat/               # Combat modules
│   │   └── movement/             # Movement modules
│   ├── settings/                  # Settings system
│   ├── events/                    # Event system
│   └── mixin/                     # Minecraft mixins
└── src/main/resources/
    ├── fabric.mod.json           # Mod metadata
    └── funnyclient.mixins.json   # Mixin configuration
```

### Adding New Modules

1. Create a new class extending `Module` in the appropriate category folder
2. Register it in `ModuleManager.java`
3. Add your module logic in `onTick()`, `onEnable()`, or `onDisable()`

Example:
```java
public class YourModule extends Module {
    public YourModule() {
        super("YourModule", "Description", Category.MISC);
    }
    
    @Override
    public void onTick() {
        // Your logic here
    }
}
```

## Roadmap 🗺️

- [ ] Click GUI system
- [ ] HUD (ArrayList, Watermark, Info displays)
- [ ] More combat modules (KillAura, Auto32k, Anchor Aura)
- [ ] More movement modules (Speed, Spider, Step)
- [ ] Render modules (ESP, Tracers, Search)
- [ ] Config system (save/load settings)
- [ ] Friend system
- [ ] Command system

## Legal Notice ⚖️

This client is designed for use on anarchy servers where such modifications are permitted. Always follow the rules of the servers you play on. We are not responsible for any bans or consequences from using this mod.

## License 📄

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Credits 🙏

- [Fabric](https://fabricmc.net/) - The modding toolchain
- [Meteor Client](https://github.com/MeteorDevelopment/meteor-client) - Inspiration and reference
- The Minecraft modding community

## Contributing 🤝

Contributions are welcome! Feel free to:
- Report bugs via [Issues](https://github.com/Schnickenpick/FunnyClient/issues)
- Submit pull requests
- Suggest new features

---

Made with ❤️ for the anarchy community
