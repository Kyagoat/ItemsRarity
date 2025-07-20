package net.Kyap.ItemsRarity.mixin;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin pour modifier la rareté retournée par ItemStack.getRarity()
 * Cela permet une compatibilité totale avec Obscure Tooltips et autres mods
 */
@Mixin(value = ItemStack.class, priority = 900)
public class ItemStackMixin {

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void injectCustomRarity(CallbackInfoReturnable<Rarity> cir) {
        try {
            ItemStack self = (ItemStack) (Object) this;
            
            // Vérifier si cet ItemStack a un tier personnalisé
            ItemTier customTier = ItemTierHelper.getItemTier(self);
            
            if (customTier != null && !customTier.getId().getPath().equals("common")) {
                // Retourner la rareté Minecraft du tier personnalisé
                // Cela permettra à Obscure Tooltips de détecter la bonne rareté
                cir.setReturnValue(customTier.getMinecraftRarity());
            }
        } catch (Exception e) {
            // En cas d'erreur, ne pas interférer avec d'autres mods
            System.err.println("ItemsRarity Mixin error: " + e.getMessage());
        }
        
        // Si pas de tier personnalisé ou erreur, laisser la méthode originale s'exécuter
    }
}
