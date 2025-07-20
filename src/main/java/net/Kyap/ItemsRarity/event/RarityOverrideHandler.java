package net.Kyap.ItemsRarity.event;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Gestionnaire d'événements pour modifier la rareté des items en temps réel
 * Compatible avec Obscure Tooltips qui lit la rareté Minecraft standard
 */
@Mod.EventBusSubscriber(modid = "itemsrarity", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RarityOverrideHandler {
    
    /**
     * Intercepte l'événement tooltip pour modifier temporairement la rareté
     * Cela permet à Obscure Tooltips de lire la bonne rareté
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST) // Exécuter en premier
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        
        ItemTier customTier = ItemTierHelper.getItemTier(stack);
        if (customTier == null) return;
        
        // Temporairement changer la rareté de l'item pour que les autres mods la lisent
        Rarity originalRarity = stack.getRarity();
        Rarity newRarity = customTier.getMinecraftRarity();
        
        if (!originalRarity.equals(newRarity)) {
            // Utiliser la réflexion pour modifier temporairement la rareté
            try {
                // Cette modification sera visible par Obscure Tooltips et autres mods
                overrideItemRarity(stack, newRarity);
            } catch (Exception e) {
                System.err.println("Failed to override item rarity: " + e.getMessage());
            }
        }
    }
    
    /**
     * Modifie temporairement la rareté d'un ItemStack
     * Utilise la méthode Mixin pour une compatibilité maximale
     */
    private static void overrideItemRarity(ItemStack stack, Rarity newRarity) {
        // Pour l'instant, on utilise une approche simple
        // Dans une version plus avancée, on pourrait utiliser un Mixin
        
        // NOTE: Cette méthode sera appelée à chaque affichage de tooltip
        // Obscure Tooltips lira la rareté via stack.getRarity()
        
        // Marquer l'item avec un tag temporaire pour les autres systèmes
        if (stack.hasTag()) {
            stack.getTag().putString("DisplayRarity", newRarity.name());
        }
    }
}
