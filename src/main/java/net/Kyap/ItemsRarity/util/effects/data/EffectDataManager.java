package net.Kyap.ItemsRarity.util.effects.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID)
public class EffectDataManager extends SimpleJsonResourceReloadListener {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<String, EffectJsonData> EFFECT_CONFIGS = new HashMap<>();

    public EffectDataManager() {
        super(GSON, "tags/effects");
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new EffectDataManager());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
                         ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        EFFECT_CONFIGS.clear();
        
        for (Map.Entry<ResourceLocation, com.google.gson.JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            
            try {
                EffectJsonData data = GSON.fromJson(entry.getValue(), EffectJsonData.class);
                EFFECT_CONFIGS.put(data.id, data);
                LOGGER.info("Loaded effect config: {}", data.id);
            } catch (JsonSyntaxException e) {
                LOGGER.error("Failed to parse effect config: {}", location, e);
            }
        }
        
        LOGGER.info("Loaded {} effect configurations", EFFECT_CONFIGS.size());
    }

    public static EffectJsonData getEffectConfig(String effectId) {
        return EFFECT_CONFIGS.get(effectId);
    }

    public static Map<String, EffectJsonData> getAllConfigs() {
        return EFFECT_CONFIGS;
    }

    public static boolean hasEffectConfig(String effectId) {
        return EFFECT_CONFIGS.containsKey(effectId);
    }
}
