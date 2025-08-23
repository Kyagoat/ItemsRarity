package net.Kyap.ItemsRarity.util.rarity.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID)
public class RarityRatesDataManager extends SimpleJsonResourceReloadListener {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static RarityRatesJsonData RATES_CONFIG;
    
    // Valeurs par défaut si le fichier JSON n'est pas trouvé
    private static final Map<String, Double> DEFAULT_CRAFTING_RATES = Map.of(
        "common", 0.50,
        "uncommon", 0.25,
        "rare", 0.15,
        "epic", 0.08,
        "legendary", 0.015,
        "mythic", 0.005
    );
    
    private static final Map<String, Double> DEFAULT_MOB_DROP_RATES = Map.of(
        "common", 0.50,
        "uncommon", 0.25,
        "rare", 0.10,
        "epic", 0.10,
        "legendary", 0.04,
        "mythic", 0.01
    );
    
    private static final Map<String, Double> DEFAULT_LOOT_CHEST_RATES = Map.of(
        "common", 0.40,
        "uncommon", 0.30,
        "rare", 0.15,
        "epic", 0.10,
        "legendary", 0.04,
        "mythic", 0.01
    );
    
    public RarityRatesDataManager() {
        super(GSON, "tags/rarities");
    }
    
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new RarityRatesDataManager());
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, 
                         @NotNull ResourceManager resourceManager, 
                         @NotNull ProfilerFiller profilerFiller) {
        RATES_CONFIG = null;
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            
            if (location.getPath().contains("drop_craft_rates")) {
                try {
                    RarityRatesJsonData data = GSON.fromJson(entry.getValue(), RarityRatesJsonData.class);
                    RATES_CONFIG = data;
                    LOGGER.info("Loaded rarity rates config: {}", data.id);
                    break;
                } catch (JsonSyntaxException e) {
                    LOGGER.error("Failed to parse rarity rates config: {}", location, e);
                }
            }
        }
        
        if (RATES_CONFIG == null) {
            LOGGER.warn("No rarity rates config found, using default values");
            RATES_CONFIG = new RarityRatesJsonData(
                "default", 
                "Default rarity rates", 
                DEFAULT_CRAFTING_RATES, 
                DEFAULT_MOB_DROP_RATES, 
                DEFAULT_LOOT_CHEST_RATES,
                Map.of() // Pas de matériaux d'amélioration par défaut
            );
        }
    }
    
    /**
     * Génère une rareté aléatoire basée sur les taux de crafting
     */
    public static ModRarities.ModRarity rollCraftingRarity() {
        Map<String, Double> rates = RATES_CONFIG != null ? RATES_CONFIG.crafting_rates : DEFAULT_CRAFTING_RATES;
        return rollRarityFromRates(rates);
    }
    
    /**
     * Génère une rareté aléatoire basée sur les taux de drop de mob
     */
    public static ModRarities.ModRarity rollMobDropRarity() {
        Map<String, Double> rates = RATES_CONFIG != null ? RATES_CONFIG.mob_drop_rates : DEFAULT_MOB_DROP_RATES;
        return rollRarityFromRates(rates);
    }
    
    /**
     * Génère une rareté aléatoire basée sur les taux de loot chest
     */
    public static ModRarities.ModRarity rollLootChestRarity() {
        Map<String, Double> rates = RATES_CONFIG != null ? RATES_CONFIG.loot_chest_rates : DEFAULT_LOOT_CHEST_RATES;
        return rollRarityFromRates(rates);
    }
    
    /**
     * Génère une rareté aléatoire basée sur un matériau d'amélioration
     * REMPLACE l'ancien RarityConfigHelper.rollNewTier()
     */
    public static ModRarities.ModRarity rollUpgradeRarity(String materialId) {
        if (RATES_CONFIG == null || RATES_CONFIG.upgrade_materials == null) {
            LOGGER.warn("No upgrade materials config found, returning COMMON");
            return ModRarities.ModRarity.COMMON;
        }
        
        Map<String, Double> materialRates = RATES_CONFIG.upgrade_materials.get(materialId.toLowerCase());
        if (materialRates == null) {
            LOGGER.warn("No config found for material: {}, returning COMMON", materialId);
            return ModRarities.ModRarity.COMMON;
        }
        
        return rollRarityFromRates(materialRates);
    }
    
    /**
     * Obtient l'ID du matériau basé sur l'item
     * REMPLACE l'ancienne méthode dans RarityConfigHelper
     */
    public static String getMaterialId(net.minecraft.world.item.ItemStack stack) {
        String itemName = stack.getItem().toString();
        if (itemName.contains("bismuth")) return "bismuth";
        if (itemName.contains("sulfur")) return "sulfur";
        if (itemName.contains("flourite")) return "flourite";
        return null;
    }
    
    /**
     * Obtient le pourcentage formaté pour une rareté donnée d'un matériau
     * REMPLACE l'ancienne méthode dans RarityConfigHelper
     */
    public static String getRarityPercentage(String materialId, String rarityName) {
        if (RATES_CONFIG == null || RATES_CONFIG.upgrade_materials == null) {
            return "0%";
        }
        
        Map<String, Double> materialRates = RATES_CONFIG.upgrade_materials.get(materialId.toLowerCase());
        if (materialRates == null || !materialRates.containsKey(rarityName.toLowerCase())) {
            return "0%";
        }
        
        double chance = materialRates.get(rarityName.toLowerCase());
        return String.format("%.0f%%", chance * 100);
    }
    
    /**
     * Obtient toutes les chances de rareté pour un matériau d'amélioration
     * Utilisé pour les tooltips
     */
    public static Map<String, Float> getUpgradeRarityChances(String materialId) {
        if (RATES_CONFIG == null || RATES_CONFIG.upgrade_materials == null) {
            return Map.of();
        }
        
        Map<String, Double> materialRates = RATES_CONFIG.upgrade_materials.get(materialId.toLowerCase());
        if (materialRates == null) {
            return Map.of();
        }
        
        Map<String, Float> result = new java.util.HashMap<>();
        for (Map.Entry<String, Double> entry : materialRates.entrySet()) {
            result.put(entry.getKey(), (float) (entry.getValue() * 100)); // Convertir en pourcentage
        }
        return result;
    }
    
    /**
     * Génère une rareté aléatoire basée sur les taux donnés
     */
    private static ModRarities.ModRarity rollRarityFromRates(Map<String, Double> rates) {
        double randomValue = ThreadLocalRandom.current().nextDouble();
        double cumulative = 0.0;
        
        // Trier par rareté (du plus commun au plus rare)
        String[] rarityOrder = {"common", "uncommon", "rare", "epic", "legendary", "mythic"};
        
        for (String rarityName : rarityOrder) {
            Double rate = rates.get(rarityName);
            if (rate != null) {
                cumulative += rate;
                if (randomValue < cumulative) {
                    return getModRarityFromName(rarityName);
                }
            }
        }
        
        // Fallback vers common si aucune correspondance
        return ModRarities.ModRarity.COMMON;
    }
    
    /**
     * Convertit un nom de rareté en ModRarity
     */
    private static ModRarities.ModRarity getModRarityFromName(String name) {
        return switch (name.toLowerCase()) {
            case "common" -> ModRarities.ModRarity.COMMON;
            case "uncommon" -> ModRarities.ModRarity.UNCOMMON;
            case "rare" -> ModRarities.ModRarity.RARE;
            case "epic" -> ModRarities.ModRarity.EPIC;
            case "legendary" -> ModRarities.ModRarity.LEGENDARY;
            case "mythic" -> ModRarities.ModRarity.MYTHIC;
            default -> ModRarities.ModRarity.COMMON;
        };
    }
    
    /**
     * Obtient la configuration actuelle (pour debug/affichage)
     */
    public static RarityRatesJsonData getCurrentConfig() {
        return RATES_CONFIG;
    }
}
