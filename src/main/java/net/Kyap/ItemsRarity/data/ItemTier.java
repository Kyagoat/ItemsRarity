package net.Kyap.ItemsRarity.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Représente un tier d'item chargé depuis les datapacks
 */
public class ItemTier {
    private final ResourceLocation id;
    private final String name;
    private final String color;
    private final Rarity minecraftRarity;
    private final int priority;
    private final double upgradeChance;
    
    public ItemTier(ResourceLocation id, String name, String color, Rarity minecraftRarity, int priority, double upgradeChance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.minecraftRarity = minecraftRarity;
        this.priority = priority;
        this.upgradeChance = upgradeChance;
    }
    
    public static ItemTier fromJson(ResourceLocation id, JsonObject json) {
        String name = json.get("name").getAsString();
        String color = json.get("color").getAsString();
        String rarityName = json.get("minecraft_rarity").getAsString();
        int priority = json.get("priority").getAsInt();
        double upgradeChance = json.has("upgrade_chance") ? json.get("upgrade_chance").getAsDouble() : 0.0;
        
        Rarity minecraftRarity = switch (rarityName.toLowerCase()) {
            case "common" -> Rarity.COMMON;
            case "uncommon" -> Rarity.UNCOMMON;
            case "rare" -> Rarity.RARE;
            case "epic" -> Rarity.EPIC;
            default -> Rarity.COMMON;
        };
        
        return new ItemTier(id, name, color, minecraftRarity, priority, upgradeChance);
    }
    
    public ResourceLocation getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public Rarity getMinecraftRarity() { return minecraftRarity; }
    public int getPriority() { return priority; }
    public double getUpgradeChance() { return upgradeChance; }
    
    @Override
    public String toString() {
        return "ItemTier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", priority=" + priority +
                '}';
    }
}
