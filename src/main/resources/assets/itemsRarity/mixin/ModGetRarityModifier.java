package net.Kyap.ItemsRarity.util;

import net.minecraft.world.item.Rarity;
import com.obscuria.obscureapi.common.items.ObscureRarity;

public class ModRarities {

    public enum ModRarity {
        COMMON("common", Rarity.COMMON),
        UNCOMMON("uncommon", Rarity.UNCOMMON),
        RARE("rare", Rarity.RARE),
        EPIC("epic", Rarity.EPIC),
        LEGENDARY("legendary", ObscureRarity.LEGENDARY),
        MYTHIC("mythic", ObscureRarity.MYTHIC);

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