package net.Kyap.ItemsRarity.client.event;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.client.tooltip.CustomTooltipRenderer;
import net.Kyap.ItemsRarity.item.ItemTransformationManager;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, value = Dist.CLIENT)
public class TooltipRenderEventHandler {

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        
        // Debug info
        System.out.println("[TOOLTIP RENDER] Item: " + stack.getItem().getDescriptionId() + ", isTransformed: " + ItemTransformationManager.isTransformedItem(stack));
        
        // Vérifier si l'item est transformé et a une rareté personnalisée
        if (ItemTransformationManager.isTransformedItem(stack)) {
            ModRarities.ModRarity customRarity = ItemTransformationManager.getCurrentRarity(stack);
            if (customRarity != null) {
                System.out.println("[TOOLTIP RENDER] Rendering custom tooltip for rarity: " + customRarity.name());
                CustomTooltipRenderer.renderCustomTooltip(event, customRarity);
                return;
            }
        }
        
        System.out.println("[TOOLTIP RENDER] No custom rarity, using default tooltip");
    }

    // Utilisons ItemTooltipEvent pour une approche plus simple (fallback)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        
        // Vérifier si l'item est transformé avec une rareté personnalisée
        if (!ItemTransformationManager.isTransformedItem(itemStack)) {
            return; // Laisser le tooltip par défaut
        }
        
        // Marquer que ce tooltip sera rendu de façon personnalisée
        // On ajoute un marqueur invisible que le système de rendu personnalisé pourra détecter
        List<Component> tooltip = event.getToolTip();
        if (!tooltip.isEmpty()) {
            // Ajouter un marqueur invisible pour identifier ce tooltip comme personnalisé
            tooltip.add(Component.literal("§r§8[CUSTOM_TOOLTIP]").withStyle(style -> style.withColor(0x00000000)));
        }
    }
}
