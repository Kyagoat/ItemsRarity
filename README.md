# Items Rarity Mod

A Minecraft 1.20.1 Forge mod that adds item rarity tiers (heavily inspired by the Tierify and Tiered mods).

This mod aims to provide an alternative to Tierify for Forge 1.20.1 modpacks.
As Tierify is not fully compatible with Connector Sinytra—which I wanted to include in my modpack. I am recreating some of its features with personnal adjustments.

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
- Compatible with mods like **Obscure Tooltip** and **Legendary Tooltip**

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
- Increased critical hit chance
- Vampirism percentage based on damage dealt
