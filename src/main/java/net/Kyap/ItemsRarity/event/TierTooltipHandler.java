package net.Kyap.ItemsRarity.event;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "itemsrarity", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TierTooltipHandler {
    
    // DÉSACTIVÉ: Obscure Tooltips prend le relais pour l'affichage des tooltips
    // Ce handler est gardé comme fallback si Obscure Tooltips n'est pas installé
    
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        // Vérifier si Obscure Tooltips est présent
        try {
            Class.forName("net.mehvahdjukaar.obscure_tooltips.ObscureTooltips");
            // Obscure Tooltips est présent, ne pas afficher notre tooltip
            return;
        } catch (ClassNotFoundException e) {
            // Obscure Tooltips n'est pas présent, utiliser notre fallback
        }
        
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        
        ItemTier tier = ItemTierHelper.getItemTier(stack);
        if (tier == null || tier.getId().getPath().equals("common")) return;
        
        // Ajouter le nom du tier avec sa couleur (fallback seulement)
        String tierName = tier.getName();
        String colorCode = tier.getColor();
        
        // Convertir le code couleur hex en ChatFormatting si possible
        ChatFormatting formatting = getFormattingFromHex(colorCode);
        
        Component tierComponent = Component.literal("[" + tierName + "]")
                .withStyle(formatting != null ? formatting : ChatFormatting.WHITE);
        
        // Ajouter le composant au début de la liste
        event.getToolTip().add(1, tierComponent);
    }
    
    private static ChatFormatting getFormattingFromHex(String hex) {
        return switch (hex.toUpperCase()) {
            case "#55FF55", "#00FF00" -> ChatFormatting.GREEN;
            case "#5555FF", "#0000FF" -> ChatFormatting.BLUE;
            case "#AA00AA", "#FF00FF" -> ChatFormatting.LIGHT_PURPLE;
            case "#FFAA00", "#FFA500" -> ChatFormatting.GOLD;
            case "#FF55FF", "#FF69B4" -> ChatFormatting.LIGHT_PURPLE;
            case "#FF0000" -> ChatFormatting.RED;
            case "#FFFFFF" -> ChatFormatting.WHITE;
            case "#FFFF00" -> ChatFormatting.YELLOW;
            case "#00FFFF" -> ChatFormatting.AQUA;
            default -> ChatFormatting.WHITE;
        };
    }
}
