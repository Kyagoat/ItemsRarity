package net.Kyap.ItemsRarity.util;

import net.minecraft.world.item.Rarity;
import com.obscuria.obscureapi.common.items.ObscureRarity;

public class ModRarities {

    public enum ModRarity {
        COMMON(Rarity.COMMON),
        UNCOMMON(Rarity.UNCOMMON),
        RARE(Rarity.RARE),
        EPIC(Rarity.EPIC),
        LEGENDARY(ObscureRarity.LEGENDARY),
        MYTHIC(ObscureRarity.MYTHIC);

        private final Rarity minecraftRarity;

        ModRarity(Rarity rarity) {
            this.minecraftRarity = rarity;
        }

        public Rarity getMinecraftRarity() {
            return minecraftRarity;
        }
    }

    public static boolean isRarityUpgradable(ModRarity rarity) {
        return rarity != ModRarity.MYTHIC;
    }
}