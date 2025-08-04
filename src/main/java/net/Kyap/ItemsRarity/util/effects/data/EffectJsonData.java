package net.Kyap.ItemsRarity.util.effects.data;

import java.util.Map;

public class EffectJsonData {
    public String id;
    public String type; // "on_hit", "on_tick", "passive", etc.
    public String tag; // "itemsrarity:sword", "itemsrarity:armor", etc.
    public String name; // Nom de l'effet pour l'affichage
    public Map<String, RarityConfig> rarity_config;

    public static class RarityConfig {
        public int weight;     // Chance d'apparition
        public float min;      // Valeur minimum de l'effet
        public float max;      // Valeur maximum de l'effet

        public RarityConfig() {}

        public RarityConfig(int weight, float min, float max) {
            this.weight = weight;
            this.min = min;
            this.max = max;
        }
    }
}
