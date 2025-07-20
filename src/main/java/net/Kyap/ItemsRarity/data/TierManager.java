package net.Kyap.ItemsRarity.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@Mod.EventBusSubscriber(modid = "itemsrarity")
public class TierManager extends SimpleJsonResourceReloadListener {
    
    private static final TierManager INSTANCE = new TierManager();
    private final Map<ResourceLocation, ItemTier> tiers = new HashMap<>();
    
    public TierManager() {
        super(new com.google.gson.Gson(), "tiers");
    }
    
    public static TierManager getInstance() {
        return INSTANCE;
    }
    
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, com.google.gson.JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        tiers.clear();
        
        for (Map.Entry<ResourceLocation, com.google.gson.JsonElement> entry : map.entrySet()) {
            ResourceLocation id = entry.getKey();
            
            try {
                if (entry.getValue().isJsonObject()) {
                    JsonObject json = entry.getValue().getAsJsonObject();
                    ItemTier tier = ItemTier.fromJson(id, json);
                    tiers.put(id, tier);
                    System.out.println("Loaded tier: " + tier);
                }
            } catch (Exception e) {
                System.err.println("Failed to load tier " + id + ": " + e.getMessage());
            }
        }
        
        System.out.println("Loaded " + tiers.size() + " item tiers");
    }
    
    public ItemTier getTier(ResourceLocation id) {
        return tiers.get(id);
    }
    
    public ItemTier getTier(String id) {
        return getTier(ResourceLocation.fromNamespaceAndPath("itemsrarity", id));
    }
    
    public Collection<ItemTier> getAllTiers() {
        return tiers.values();
    }
    
    public ItemTier getTierByPriority(int priority) {
        return tiers.values().stream()
                .filter(tier -> tier.getPriority() == priority)
                .findFirst()
                .orElse(null);
    }
    
    public ItemTier getNextTier(ItemTier currentTier) {
        return getTierByPriority(currentTier.getPriority() + 1);
    }
}
