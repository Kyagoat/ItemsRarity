package net.Kyap.ItemsRarity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class ModRarities {

    public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);
    public static final Rarity MYTHIC = Rarity.create("mythic", ChatFormatting.RED);

    public enum ModRarity {
        COMMON("common", Rarity.COMMON, 0),
        UNCOMMON("uncommon", Rarity.UNCOMMON, 1),
        RARE("rare", Rarity.RARE, 2),
        EPIC("epic", Rarity.EPIC, 3),
        LEGENDARY("legendary", ModRarities.LEGENDARY, 4),
        MYTHIC("mythic", ModRarities.MYTHIC, 5);

        private final String id;
        private final Rarity rarity;
        private final int level;

        ModRarity(String id, Rarity rarity, int level) {
            this.id = id;
            this.rarity = rarity;
            this.level = level;
        }

        public String getId() { return id; }
        public Rarity getRarity() { return rarity; }
        public int getLevel() { return level; }

        public static ModRarity fromId(String id) {
            for (ModRarity r : values()) {
                if (r.id.equalsIgnoreCase(id)) return r;
            }
            return null;
        }
    }
}