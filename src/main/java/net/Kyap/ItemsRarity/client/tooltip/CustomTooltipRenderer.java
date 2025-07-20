package net.Kyap.ItemsRarity.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.item.ItemTransformationManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomTooltipRenderer {
    
    // Dimensions des bordures (en pixels)
    private static final int BORDER_SIZE = 8;
    private static final int CORNER_SIZE = 16;
    
    // ResourceLocations pour les textures de bordure - avec fallback vers texture par défaut
    private static final ResourceLocation DEFAULT_BORDER = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
    private static final ResourceLocation COMMON_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/common_border.png");
    private static final ResourceLocation UNCOMMON_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/uncommon_border.png");
    private static final ResourceLocation RARE_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/rare_border.png");
    private static final ResourceLocation EPIC_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/epic_border.png");
    private static final ResourceLocation LEGENDARY_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/legendary_border.png");
    private static final ResourceLocation MYTHIC_BORDER = new ResourceLocation(ItemsRarity.MOD_ID, "textures/gui/tooltip/mythic_border.png");
    
    /**
     * Obtient la texture de bordure selon la rareté
     */
    public static ResourceLocation getBorderTexture(ModRarities.ModRarity rarity) {
        return switch (rarity) {
            case COMMON -> COMMON_BORDER;
            case UNCOMMON -> UNCOMMON_BORDER;
            case RARE -> RARE_BORDER;
            case EPIC -> EPIC_BORDER;
            case LEGENDARY -> LEGENDARY_BORDER;
            case MYTHIC -> MYTHIC_BORDER;
        };
    }
    
    /**
     * Rend des décorations simples autour du tooltip (version simplifiée)
     */
    public static void renderSimpleCustomBorder(GuiGraphics guiGraphics, ItemStack itemStack, int tooltipX, int tooltipY, int tooltipWidth, int tooltipHeight) {
        if (!net.Kyap.ItemsRarity.item.ItemTransformationManager.isTransformedItem(itemStack)) {
            return;
        }
        
        ModRarities.ModRarity rarity = net.Kyap.ItemsRarity.item.ItemTransformationManager.getCurrentRarity(itemStack);
        if (rarity == null) return;
        
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        
        // Couleurs selon la rareté
        int color = switch (rarity) {
            case COMMON -> 0xFFFFFFFF;      // Blanc
            case UNCOMMON -> 0xFFFFFF55;    // Jaune
            case RARE -> 0xFF55FFFF;        // Cyan
            case EPIC -> 0xFFAA00AA;        // Violet
            case LEGENDARY -> 0xFFFF5555;   // Rouge
            case MYTHIC -> 0xFFFF55FF;      // Magenta
        };
        
        // Dessiner un cadre coloré simple
        int borderWidth = 2;
        int x = tooltipX - borderWidth;
        int y = tooltipY - borderWidth;
        int totalWidth = tooltipWidth + (borderWidth * 2);
        int totalHeight = tooltipHeight + (borderWidth * 2);
        
        // Bordure haut
        guiGraphics.fill(x, y, x + totalWidth, y + borderWidth, color);
        // Bordure bas
        guiGraphics.fill(x, y + totalHeight - borderWidth, x + totalWidth, y + totalHeight, color);
        // Bordure gauche
        guiGraphics.fill(x, y, x + borderWidth, y + totalHeight, color);
        // Bordure droite
        guiGraphics.fill(x + totalWidth - borderWidth, y, x + totalWidth, y + totalHeight, color);
        
        poseStack.popPose();
        
        System.out.println("[TOOLTIP] Rendered custom border for " + rarity.name() + " item");
    }
    
    /**
     * Point d'entrée principal pour le rendu de tooltip personnalisé
     */
    public static void renderCustomTooltip(net.minecraftforge.client.event.RenderTooltipEvent.Pre event, ModRarities.ModRarity rarity) {
        // Pour l'instant, on utilise la version simple avec bordures colorées
        // Les coordonnées du tooltip sont disponibles dans l'event
        ItemStack itemStack = event.getItemStack();
        
        // Pour RenderTooltipEvent.Pre, nous devons personnaliser le rendu
        // Ici on pourrait modifier les couleurs de fond, mais pour simplifier,
        // on va juste laisser passer et ajouter nos décorations en post
        System.out.println("[CUSTOM TOOLTIP] Preparing custom tooltip for " + rarity.name());
    }
    
    /**
     * Version future avec textures PNG (à implémenter quand les PNG seront prêts)
     */
    public static void renderCustomTooltipBorder(GuiGraphics guiGraphics, ItemStack itemStack, int tooltipX, int tooltipY, int tooltipWidth, int tooltipHeight) {
        // Pour l'instant, utiliser la version simple
        renderSimpleCustomBorder(guiGraphics, itemStack, tooltipX, tooltipY, tooltipWidth, tooltipHeight);
        
        // TODO: Implémenter le rendu PNG quand les textures seront disponibles
        /*
        if (!RarityManager.hasCustomRarity(itemStack)) {
            return;
        }
        
        ModRarities.ModRarity rarity = RarityManager.getCustomRarity(itemStack);
        if (rarity == null) return;
        
        ResourceLocation borderTexture = getBorderTexture(rarity);
        
        // Code de rendu PNG complet ici...
        */
    }
    
    /**
     * Calcule l'espace supplémentaire nécessaire pour les bordures
     */
    public static int getExtraBorderSize() {
        return 2; // Version simple utilise 2 pixels
    }
    
    /**
     * Vérifie si un item devrait avoir un tooltip personnalisé
     */
    public static boolean shouldUseCustomTooltip(ItemStack itemStack) {
        return net.Kyap.ItemsRarity.item.ItemTransformationManager.isTransformedItem(itemStack);
    }
}
