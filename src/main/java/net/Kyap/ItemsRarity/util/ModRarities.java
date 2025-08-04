package net.Kyap.ItemsRarity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class ModRarities {

    public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);
    public static final Rarity MYTHIC = Rarity.create("mythic", ChatFormatting.RED);

    public enum ModRarity {
        COMMON("common", Rarity.COMMON),
        UNCOMMON("uncommon", Rarity.UNCOMMON),
        RARE("rare", Rarity.RARE),
        EPIC("epic", Rarity.EPIC),
        LEGENDARY("legendary", ModRarities.LEGENDARY),
        MYTHIC("mythic", ModRarities.MYTHIC);

        private final String id;
        private final Rarity rarity;

        ModRarity(String id, Rarity rarity) {
            this.id = id;
            this.rarity = rarity;
        }

        public String getId() {
            return id;
        }

        public Rarity getRarity() {
            return rarity;
        }

        // Pour retrouver un ModRarity depuis un tag (ex: "epic")
        public static ModRarity fromId(String id) {
            for (ModRarity r : values()) {
                if (r.id.equalsIgnoreCase(id)) return r;
            }
            return null;
        }
    }
}