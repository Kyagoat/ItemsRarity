package net.Kyap.ItemsRarity.util.rarity.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID)
public class RarityDataManager extends SimpleJsonResourceReloadListener {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<String, RarityJsonData> RARITY_CONFIGS = new HashMap<>();

    public RarityDataManager() {
        super(GSON, "rarity_materials");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager,
                         @NotNull ProfilerFiller profilerFiller) {
        RARITY_CONFIGS.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            
            try {
                RarityJsonData data = GSON.fromJson(entry.getValue(), RarityJsonData.class);
                RARITY_CONFIGS.put(data.id, data);
                LOGGER.info("Loaded rarity material config: {}", data.id);
            } catch (JsonSyntaxException e) {
                LOGGER.error("Failed to parse rarity material config: {}", location, e);
            }
        }
        
        LOGGER.info("Loaded {} rarity material configurations", RARITY_CONFIGS.size());
    }

    public static RarityJsonData getRarityConfig(String rarityId) {
        return RARITY_CONFIGS.get(rarityId);
    }

}
