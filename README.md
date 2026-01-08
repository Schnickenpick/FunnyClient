# FunnyClient 🎮

A hilarious Minecraft utility client for anarchy servers like 2b2t!

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-brightgreen)
![Fabric](https://img.shields.io/badge/Fabric-0.16.5-orange)
![License](https://img.shields.io/badge/License-GPL--3.0-blue)

## Features ✨

### Combat Modules
- **Crystal Aura** ⚡ - Automatically places and breaks end crystals
  - Smart damage calculation
  - Auto-target nearest player
  - Min/max damage settings
  - Auto-switch to crystals
  - Rotation support

### Movement Modules
- **Flight** ✈️ - Multiple flight modes for maximum mobility
  - Vanilla mode (standard fly)
  - Packet mode (bypass anti-cheat)
  - Creative mode (full creative flight)
  - Configurable speed and vertical speed

### Player Modules
- **Nuker** 💥 - Automatically breaks blocks around you
  - Configurable range
  - Rotation support
  - Instant packet mode or normal breaking
- **Reach** 🤚 - Extend your reach distance
  - Up to 6.0 blocks reach
  - Configurable distance
- **Scaffold** 🧱 - Automatically places blocks under you
  - Auto-switch to blocks
  - Rotation support
  - Configurable delay

### Render Modules
- **ESP** 👁️ - See entities through walls
  - Players highlighting
  - Mobs highlighting
  - Configurable range (up to 256 blocks)
  - Color-coded entities
- **Tracers** 📍 - Draw lines to entities
  - Lines from player to entities
  - Players and mobs support
  - Configurable range
- **Fullbright** 💡 - See in the dark
  - Maximum gamma for night vision
  - No potion effects needed

### GUI System
- **Click GUI** 🖱️ - Easy-to-use interface
  - Press **RIGHT_SHIFT** to open
  - Draggable category panels
  - Click modules to toggle
  - Right-click to expand settings
  - Live setting adjustments
  - Meteor-inspired design

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

### Opening the GUI
- Press **RIGHT_SHIFT** to open the Click GUI
- Drag panels by the header
- Left-click modules to toggle them
- Right-click modules to expand settings
- Right-click category headers to collapse panels

### Module Settings Examples
- **Flight**: Mode (Vanilla/Packet/Creative), Speed, Vertical Speed
- **Crystal Aura**: Range, Place Range, Auto Switch, Rotate, Min/Max Damage
- **ESP**: Players, Mobs, Range
- **Nuker**: Range, Rotate, Instant mode
- **Scaffold**: Rotate, Auto Switch, Delay

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
│   │   ├── movement/             # Movement modules
│   │   ├── player/               # Player modules
│   │   └── render/               # Render modules
│   ├── gui/                       # Click GUI system
│   ├── settings/                  # Settings system
│   ├── events/                    # Event system
│   ├── utils/                     # Utility classes
│   │   ├── PlayerUtils.java
│   │   ├── RotationUtils.java
│   │   ├── BlockUtils.java
│   │   ├── InventoryUtils.java
│   │   ├── WorldUtils.java
│   │   └── RenderUtils.java
│   └── mixin/                     # Minecraft mixins
└── src/main/resources/
    ├── fabric.mod.json           # Mod metadata
    └── funnyclient.mixins.json   # Mixin configuration
```

### Utility Classes

FunnyClient includes comprehensive utility classes to make module development easier:

- **PlayerUtils** - Player distance, positioning, and entity queries
- **RotationUtils** - Calculate and apply rotations to targets
- **BlockUtils** - Block queries, sphere generation, and placement checks
- **InventoryUtils** - Item finding and hotbar management
- **WorldUtils** - Entity queries and world interactions
- **RenderUtils** - ESP boxes, tracers, and custom rendering

### Adding New Modules

1. Create a new class extending `Module` in the appropriate category folder
2. Register it in `ModuleManager.java`
3. Add your module logic in `onTick()`, `onEnable()`, or `onDisable()`

Example:
```java
public class YourModule extends Module {
    private final DoubleSetting setting = new DoubleSetting("Setting", 1.0, 0.1, 10.0, 0.1);
    
    public YourModule() {
        super("YourModule", "Description", Category.MISC);
        getSettings().add(setting);
    }
    
    @Override
    public void onTick() {
        // Your logic here using utility classes
        if (mc.player == null) return;
        
        PlayerEntity target = PlayerUtils.getClosestPlayer(10.0);
        if (target != null) {
            RotationUtils.rotateTo(target);
        }
    }
}
```

## Roadmap 🗺️

- [x] Click GUI system
- [x] Combat modules (Crystal Aura)
- [x] Movement modules (Flight)
- [x] Player modules (Nuker, Reach, Scaffold)
- [x] Render modules (ESP, Tracers, Fullbright)
- [x] Utility helpers (Player, Rotation, Block, Inventory, World, Render)
- [ ] HUD (ArrayList, Watermark, Info displays)
- [ ] More combat modules (KillAura, Auto32k, Anchor Aura, AutoTotem)
- [ ] More movement modules (Speed, Spider, Step, NoFall)
- [ ] Config system (save/load settings)
- [ ] Friend system
- [ ] Command system
- [ ] Waypoints

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
