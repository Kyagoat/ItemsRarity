package net.Kyap.ItemsRarity.util.rarity.data;

import java.util.Map;

public class RarityRatesJsonData {
    public String id;
    public String description;
    public Map<String, Double> crafting_rates;
    public Map<String, Double> mob_drop_rates;
    public Map<String, Double> loot_chest_rates;
    public Map<String, Map<String, Double>> upgrade_materials;
    
    public RarityRatesJsonData() {
        // Constructeur par défaut pour Gson
    }
    
    public RarityRatesJsonData(String id, String description, 
                              Map<String, Double> craftingRates,
                              Map<String, Double> mobDropRates,
                              Map<String, Double> lootChestRates,
                              Map<String, Map<String, Double>> upgradeMaterials) {
        this.id = id;
        this.description = description;
        this.crafting_rates = craftingRates;
        this.mob_drop_rates = mobDropRates;
        this.loot_chest_rates = lootChestRates;
        this.upgrade_materials = upgradeMaterials;
    }
}
