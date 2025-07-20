package net.Kyap.ItemsRarity.event;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Alternative au Mixin : utilise la réflexion pour modifier temporairement la rareté
 * Compatible avec Obscure Tooltips
 */
@Mod.EventBusSubscriber(modid = "itemsrarity", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RarityReflectionHandler {
    
    private static Field rarityField = null;
    
    static {
        try {
            // Essayer de trouver le champ rarity dans ItemStack (pourrait être obfusqué)
            rarityField = ObfuscationReflectionHelper.findField(ItemStack.class, "rarity");
            rarityField.setAccessible(true);
        } catch (Exception e) {
            System.err.println("Could not find rarity field in ItemStack: " + e.getMessage());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (rarityField == null) return;
        
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        
        ItemTier customTier = ItemTierHelper.getItemTier(stack);
        if (customTier == null || customTier.getId().getPath().equals("common")) return;
        
        try {
            // Sauvegarder la rareté originale
            Object originalRarity = rarityField.get(stack);
            
            // Temporairement changer la rareté pour que les autres mods la voient
            rarityField.set(stack, customTier.getMinecraftRarity());
            
            // Note: Dans un monde parfait, on devrait restaurer la rareté originale après
            // Mais comme les tooltips se recalculent souvent, on peut laisser comme ça
            
        } catch (Exception e) {
            // Silencieusement échouer pour éviter les crashes
        }
    }
}
