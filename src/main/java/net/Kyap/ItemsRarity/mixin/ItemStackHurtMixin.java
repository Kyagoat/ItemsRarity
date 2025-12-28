package net.Kyap.ItemsRarity.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackHurtMixin {

    @ModifyVariable(
            method = "hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int modifyDurabilityAmount(int amount, int originalAmount, RandomSource random, ServerPlayer player) {
        ItemStack stack = (ItemStack)(Object)this;

        if (!stack.hasTag()) {
            return amount;
        }

        assert stack.getTag() != null;
        if (stack.getTag().contains("DurabilityEffect")) {
            float modifier = stack.getTag().getFloat("DurabilityEffect");
            float multiplier = 1.0f - modifier;
            
            int newAmount;
            if (multiplier > 1.0f) {
                newAmount = (int)Math.max(1, Math.ceil(amount * multiplier));
            } else {
                newAmount = Math.max(1, Math.round(amount * multiplier));
            }
            return newAmount;
        }
        return amount;
    }
}

