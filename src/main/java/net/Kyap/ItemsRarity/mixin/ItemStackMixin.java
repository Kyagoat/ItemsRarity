package net.Kyap.ItemsRarity.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void onGetRarity(CallbackInfoReturnable<Rarity> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains("rarity")) {
                String rarityId = stack.getTag().getString("rarity").toLowerCase();

                Rarity rarity = switch (rarityId) {
                    case "uncommon" -> Rarity.UNCOMMON;
                    case "rare" -> Rarity.RARE;
                    case "epic" -> Rarity.EPIC;
                    case "legendary" -> Rarity.EPIC; // Ou ta rareté custom si elle existe
                    case "mythic" -> Rarity.EPIC;   // Idem
                    default -> Rarity.COMMON;
                };

                cir.setReturnValue(rarity);
            }
        }
    }
}