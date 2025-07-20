package net.Kyap.ItemsRarity.util;

import net.Kyap.ItemsRarity.item.custom.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire pour transformer les items avec des raretés personnalisées
 */
public class ItemTransformationManager {
    
    // Cache des items transformés pour éviter de recréer les mêmes instances
    private static final ConcurrentHashMap<String, Item> TRANSFORMED_ITEMS_CACHE = new ConcurrentHashMap<>();
    
    /**
     * Transforme un ItemStack vers une version avec la rareté spécifiée
     */
    public static ItemStack transformItemWithRarity(ItemStack originalStack, ModRarities.ModRarity newRarity) {
        if (originalStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        // Si l'item a déjà la bonne rareté, pas besoin de transformation
        if (getCurrentRarity(originalStack) == newRarity) {
            return originalStack.copy();
        }
        
        Item originalItem = getOriginalItem(originalStack);
        Item transformedItem = getOrCreateTransformedItem(originalItem, newRarity);
        
        // Créer le nouvel ItemStack avec l'item transformé
        ItemStack newStack = new ItemStack(transformedItem, originalStack.getCount());
        
        // Copier tous les NBT tags (enchantements, durabilité, etc.)
        if (originalStack.hasTag()) {
            newStack.setTag(originalStack.getTag().copy());
        }
        
        // Marquer comme item transformé pour pouvoir retrouver l'original
        CompoundTag tag = newStack.getOrCreateTag();
        tag.putString("OriginalItem", originalItem.getDescriptionId());
        tag.putString("CustomRarity", newRarity.name());
        
        return newStack;
    }
    
    /**
     * Obtient ou crée un item transformé avec la rareté spécifiée
     */
    private static Item getOrCreateTransformedItem(Item originalItem, ModRarities.ModRarity rarity) {
        String cacheKey = originalItem.getDescriptionId() + "_" + rarity.name();
        
        return TRANSFORMED_ITEMS_CACHE.computeIfAbsent(cacheKey, key -> {
            return switch (rarity) {
                case COMMON -> originalItem; // Pas de transformation pour COMMON
                case UNCOMMON -> new UncommonItem(originalItem);
                case RARE -> new RareItem(originalItem);
                case EPIC -> new EpicItem(originalItem);
                case LEGENDARY -> new LegendaryItem(originalItem);
                case MYTHIC -> new MythicItem(originalItem);
            };
        });
    }
    
    /**
     * Récupère l'item original depuis un ItemStack transformé
     */
    public static Item getOriginalItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        
        // Si c'est un item transformé
        if (stack.getItem() instanceof RarityItem rarityItem) {
            return rarityItem.getOriginalItem();
        }
        
        // Si l'item a des NBT tags avec l'item original
        if (stack.hasTag() && stack.getTag().contains("OriginalItem")) {
            String originalItemId = stack.getTag().getString("OriginalItem");
            // Ici on pourrait retrouver l'item par son ID si nécessaire
            // Pour l'instant on retourne l'item actuel
        }
        
        return stack.getItem();
    }
    
    /**
     * Récupère la rareté actuelle d'un ItemStack
     */
    public static ModRarities.ModRarity getCurrentRarity(ItemStack stack) {
        if (stack.isEmpty()) {
            return ModRarities.ModRarity.COMMON;
        }
        
        // Si c'est un item transformé
        if (stack.getItem() instanceof RarityItem rarityItem) {
            return rarityItem.getCustomRarity();
        }
        
        // Si l'item a des NBT tags avec la rareté
        if (stack.hasTag() && stack.getTag().contains("CustomRarity")) {
            String rarityName = stack.getTag().getString("CustomRarity");
            try {
                return ModRarities.ModRarity.valueOf(rarityName);
            } catch (IllegalArgumentException e) {
                // Rareté invalide, retourner COMMON
            }
        }
        
        // Convertir depuis la rareté Minecraft
        return convertFromMinecraftRarity(stack.getRarity());
    }
    
    /**
     * Convertit une rareté Minecraft vers notre enum
     */
    private static ModRarities.ModRarity convertFromMinecraftRarity(net.minecraft.world.item.Rarity rarity) {
        for (ModRarities.ModRarity modRarity : ModRarities.ModRarity.values()) {
            if (modRarity.getMinecraftRarity().equals(rarity)) {
                return modRarity;
            }
        }
        return ModRarities.ModRarity.COMMON;
    }
    
    /**
     * Vérifie si un ItemStack est un item transformé
     */
    public static boolean isTransformedItem(ItemStack stack) {
        return !stack.isEmpty() && (
            stack.getItem() instanceof RarityItem ||
            (stack.hasTag() && stack.getTag().contains("OriginalItem"))
        );
    }
}
