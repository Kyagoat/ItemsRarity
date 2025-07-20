package net.Kyap.ItemsRarity.event;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, value = Dist.CLIENT)
public class ItemRarityEventHandler {
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        
        // Debug - afficher pour tous les items
        System.out.println("[TOOLTIP DEBUG] Item: " + stack.getItem().getDescriptionId() + ", isTransformed: " + net.Kyap.ItemsRarity.item.ItemTransformationManager.isTransformedItem(stack));
        
        // Si l'item a une rareté personnalisée, on laisse le système de rendu personnalisé s'en occuper
        if (net.Kyap.ItemsRarity.item.ItemTransformationManager.isTransformedItem(stack)) {
            ModRarities.ModRarity customRarity = net.Kyap.ItemsRarity.item.ItemTransformationManager.getCurrentRarity(stack);
            if (customRarity != null) {
                // Ajouter seulement les informations de debug, le rendu visuel est géré par TooltipRenderEventHandler
                event.getToolTip().add(Component.literal("Custom Rarity: " + customRarity.name())
                        .withStyle(ChatFormatting.GRAY));
                
                event.getToolTip().add(Component.literal("Display Rarity: " + customRarity.getMinecraftRarity().name())
                        .withStyle(customRarity.getMinecraftRarity().getStyleModifier()));
                        
                System.out.println("[EVENT] Custom rarity " + customRarity.name() + " detected, custom tooltip will be rendered");
            }
        }
    }
}
