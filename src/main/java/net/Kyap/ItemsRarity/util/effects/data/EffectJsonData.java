package net.Kyap.ItemsRarity.util.effects.data;

import java.util.Map;

public class EffectJsonData {
    public String id;
    public String type;
    public String tag;
    public String name;
    public Map<String, RarityConfig> rarity_config;

    public static class RarityConfig {
        public int weight;
        public float min;
        public float max;

        public RarityConfig() {}

        public RarityConfig(int weight, float min, float max) {
            this.weight = weight;
            this.min = min;
            this.max = max;
        }
    }
}
