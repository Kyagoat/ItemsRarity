# Items Rarity Mod

A Minecraft 1.20.1 Forge mod that adds a custom rarity system for weapons and armor.

## Features

- **Custom Rarity System**
Weapons and armor can have multiple rarity levels when dropped or looted:
  - Common (White) - 50% chance
  - Uncommon (Green) - 25% chance  
  - Rare (Blue) - 10% chance
  - Epic (Purple) - 5% chance
  - Legendary (Yellow) - 1% chance *(requires Obscure API)*
  - Mythic (Black) - 0.5% chance *(requires Obscure API)*
  - Shadow (Dark Gray) - Custom rarity with special tooltip effects

- **Advanced Tooltip System**
  - Custom visual effects for Shadow rarity using Obscure Tooltips
  - Rim lighting effects and custom color panels
  - Descent shine icons and enhanced visual presentation
  - Centralized rarity management through ModRarities utility class

- **Enhanced Rarity Management**
  - ModRarities class provides centralized access to all rarities
  - Full translation support for custom rarities
  - Seamless integration with Obscure API for extended rarities

## 🔧 Item Rarity Upgrade System

Weapons and armor can be upgraded in rarity using a special **Upgraded Anvil**.

Each upgrade consumes:
- 1 rarity material (Manganese, Bismuth, or Osmium)
- 1 base material related to the item being upgraded (e.g., iron ingot for iron items)
- The item itself

If the item has no known recipe, a default material (pretty rare) from this mod will be used instead.

### 🛠️ Crafting the Upgraded Anvil

The Upgraded Anvil is required to perform upgrades and is crafted with:
- 1 Vanilla Anvil (undamaged)
- 4 Manganese
- 3 Bismuth  
- 1 Osmium

### 💎 How Upgrading Works

Every upgrade attempt uses a rarity material that defines the maximum rarity it can reach.

On use, the system randomly determines the new rarity, from the current level up to the max allowed by the material.

Default success chances are always applied, regardless of the item's current rarity.

Items can never downgrade — they either improve or stay at their current rarity.

This means you can potentially jump multiple rarity tiers in a single upgrade, depending on the material used and your luck.

### 🧱 Rarity Materials & Their Upgrade Ranges

| Material  | Possible Rarity Outcomes          |
|-----------|-----------------------------------|
| Manganese | Common / Uncommon / Rare          |
| Osmium    | Rare / Epic / Legendary           |
| Bismuth   | Rare / Epic / Legendary / Mythic  |

⚠️ **The higher the rarity tier, the lower the probability of obtaining it.**

- **Automatic Rarity Assignment**: Items get rarity when:
  - Drop by mobs
  - Found in loot chests

- **Gameplay Effects**:
  - Weapons deal bonus damage based on rarity
  - Armor provides bonus protection based on rarity
  - Colored item names in tooltips
  - Detailed rarity information in tooltips

- **Configuration**: Fully configurable rarity chances and features via config file

## Commands

All commands require operator permissions (level 2):

- `/rarity apply <rarity>` - Apply specific rarity to held item
- `/rarity random` - Apply random rarity to held item  
- `/rarity remove` - Remove rarity from held item
- `/rarity info` - Show rarity info for held item

## Compatible Items

The mod works with:
- All vanilla weapons (swords, axes, tridents)
- All vanilla armor pieces
- All vanilla tools (pickaxes, shovels, hoes)
- Ranged weapons (bows, crossbows)
- Any modded items that extend the same base classes

### Custom Items
- **Bismuth**: Custom item with Shadow rarity featuring enhanced tooltip effects
- Items utilizing the ModRarities system for consistent rarity management

## Installation

1. Install Minecraft Forge for 1.20.1 (version 47.4.0 or later)
2. Place the mod JAR file in your mods folder
3. Launch Minecraft

## Configuration

The mod creates a config file at `config/itemsrarity-common.toml` where you can:
- Enable/disable the rarity system
- Adjust rarity chances
- Toggle tooltip features
- Toggle colored item names

## Building from Source

1. Clone this repository
2. Run `./gradlew build`
3. Find the JAR in `build/libs/`

## License

All Rights Reserved
