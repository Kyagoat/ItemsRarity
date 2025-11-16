# Items Rarity Mod

A Minecraft 1.20.1 Forge mod that adds item rarity tiers (heavily inspired by the Tierify and Tiered mods).

This mod aims to provide an alternative to Tierify for Forge 1.20.1 modpacks.
Since Tierify is not fully compatible with Connector Sinytra—which I wanted to include in my modpack. I decided to recreate some of its features with inspired adjustments.
I wanted a mod like Tierify for my RPG / SMP / RP modpack for me and my friends, so I decided to make my first mod.

## Item Rarity Upgrade System

Weapons and armor can be upgraded in rarity using a special **Upgraded Anvil**.

Each upgrade consumes:
- 1 rarity material (Manganese, Bismuth, or Osmium)
- 1 base material related to the item being upgraded (e.g., iron ingot for iron items)
- The item itself
- An emerald hammer craftable with 3 emeralds blocks and 2 sticks
- 
This feature is meant to introduce resource destruction in the modpack, which is one of the main reasons I started coding this mod.

If the item has no known recipe, a default rare material from this mod will be used instead (currently a feather as a placeholder, to be replaced later).

### Crafting the Upgraded Anvil

The Upgraded Anvil is required for performing upgrades and is crafted using:
- 1 Vanilla Anvil (undamaged)
- 4 Manganese
- 3 Bismuth
- 1 Osmium

### Advanced Tooltip System

- Custom visual effects
- Compatible with mods like **Obscure Tooltip** or **Legendary Tooltips**.

---

## How Upgrading Works

### Rarity Materials & Their Upgrade Ranges

| Material  | Possible Rarity Outcomes                |
|-----------|------------------------------------------|
| Flourite  | Common / Uncommon / Rare / Epic         |
| Sulfur    | Uncommon / Rare / Epic / Legendary      |
| Bismuth   | Rare / Epic / Legendary / Mythic        |

The default rarity chances are defined in the config file

> Both the rarity tiers and their probability rates are **fully data-driven**, meaning you can add, remove, or tweak them as needed.

---

## Automatic Rarity Assignment

The default rarity chances are defined in the config file


## Gameplay Effects (WIP)

Effects applied to weapons:
- Decreased critical hit chance
- Increased critical hit chance
- Decreased attack damage
- Increase attack damage
- Decreased durability

---

## Custom Critical Hit System

The default Minecraft critical hit mechanic is replaced by a custom system:

- You can no longer perform regular critical hits in the usual way (this may become data-driven to allow toggling).
- To enhance your crit chances, you'll need to acquire specific effects tied to gear rarity or modifiers.

The long-term goal is to implement a wide variety of effects for both weapons and armor, allowing players to build and optimize their own unique playstyles.

---

## Roadmap

- Add more effects to gear, while keeping them balanced and not overpowered compared to Minecraft's default enchantment system.
- Make more systems data-driven to allow easier customization by others.
- If possible, redesign the textures for the anvil and ores.
- I also plan to add resource blocks for the ores. In my modpack, these resources will also be obtainable as drops from bosses (and from regular mobs for the less rare ones). They will be mineable as well, with drop chances that reflect their rarity.
- Mayby adding a customizable JSON to add ores drop to popular mobs mod (bosses first). (Might be overkill)

> Current ores textures are taken from this repo: https://github.com/malcolmriley/unused-textures  
> The artist's work is amazing, but I would love to have more personal textures eventually.

---

## Credits

Special thanks to [malcolmriley](https://github.com/malcolmriley) for the placeholder textures used for ores.  
Textures are from the **unused-textures** repository: https://github.com/malcolmriley/unused-textures  
Used under the terms of the repository's license.
(The texture of the enhanced anvil is one of mine which explained why it is shitty.)

---

Any suggestion or feedback is welcome.  
As I said above, this is my first mod and I'm trying to improve, so feel free to share criticism.

Feel free to use or modify this mod as needed.

This README was written by me and reviewed with the help of an AI, as English is not my first language.
