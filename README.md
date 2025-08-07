# Items Rarity Mod

A Minecraft 1.20.1 Forge mod that adds item rarity tiers (heavily inspired by the Tierify and Tiered mods).

This mod aims to provide an alternative to Tierify for Forge 1.20.1 modpacks.
As Tierify is not fully compatible with Connector Sinytra—which I wanted to include in my modpack—I am recreating some of its features with inspired adjustments.

## Item Rarity Upgrade System

Weapons and armor can be upgraded in rarity using a special **Upgraded Anvil**.

Each upgrade consumes:
- 1 rarity material (Manganese, Bismuth, or Osmium)
- 1 base material related to the item being upgraded (e.g., iron ingot for iron items)
- The item itself

If the item has no known recipe, a default rare material from this mod will be used instead (currently a feather as a placeholder, to be replaced later).

### Crafting the Upgraded Anvil

The Upgraded Anvil is required for performing upgrades and is crafted using:
- 1 Vanilla Anvil (undamaged)
- 4 Manganese
- 3 Bismuth
- 1 Osmium

### Advanced Tooltip System

- Custom visual effects
- Compatible with mods like **Obscure Tooltip** and **Legendary Tooltip** via mixins

---

## How Upgrading Works

### Rarity Materials & Their Upgrade Ranges

| Material  | Possible Rarity Outcomes                |
|-----------|------------------------------------------|
| Manganese | Common / Uncommon / Rare / Epic         |
| Osmium    | Uncommon / Rare / Epic / Legendary      |
| Bismuth   | Rare / Epic / Legendary / Mythic        |

The default rarity chances are defined as follows.

Example for Bismuth:
{id: "bismuth", name: "Bismuth", rarity_chances: {
  "rare": 0.35,
  "epic": 0.45,
  "legendary": 0.15,
  "mythic": 0.05
}}

> Both the rarity tiers and their probability rates are **fully data-driven**, meaning you can add, remove, or tweak them as needed.

**The higher the rarity tier, the lower the chance of obtaining it.**

---

## Automatic Rarity Assignment *(to be implemented)*

Items will automatically receive a rarity when:
- Dropped by mobs
- Found in loot chests

---

## Gameplay Effects

Currently, effects are only applied to weapons:
- Decrease critical hit chance
- Increased critical hit chance
- Vampirism percentage based on damage dealt

## Rarity Effects System

Rarity not only grants gameplay effects to gear, but also unlocks a limited number of effect slots:

- **Common** and **Uncommon**: 1 slot, with more malus than bonus effects
- **Rare** and **Epic**: 2 slots, with more bonus than malus effects
- **Legendary**: 3 slots, only bonuses with access to rarer effects
- **Mythic**: 4 slots, only bonuses with higher chances of powerful effects

## Custom Critical Hit System

The default Minecraft critical hit mechanic is replaced by a custom system:

- You can no longer perform regular critical hits in the usual way (this may become data-driven to choose weither or not you activate it).
- To enhance your crit chances, you'll need to acquire specific effects tied to gear rarity or modifiers.

The long-term goal is to implement a wide variety of effects for both weapons and armor, allowing players to build and optimize their own unique playstyles.
