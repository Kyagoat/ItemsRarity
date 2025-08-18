package net.Kyap.ItemsRarity.mixin;

import net.Kyap.ItemsRarity.ItemsRarity;
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

        // Debug: toujours logger quand le mixin s'exécute
        System.out.println("[ItemStackHurtMixin] Mixin called with amount: " + amount + " for item: " + stack.getDisplayName().getString());

        if (!stack.hasTag()) {
            System.out.println("[ItemStackHurtMixin] No tag found on item");
            return amount;
        }

        // Récupérer le multiplicateur de ton effet custom
        assert stack.getTag() != null;
        if (stack.getTag().contains("DurabilityEffect")) {
            float modifier = stack.getTag().getFloat("DurabilityEffect");
            System.out.println("[ItemStackHurtMixin] Found DurabilityEffect: " + modifier);
            
            // Logique corrigée : 
            // -0.40 = +40% de dégâts de durabilité (moins durable, mauvais effet)
            // +0.40 = -40% de dégâts de durabilité (plus durable, bon effet)
            float multiplier = 1.0f - modifier;
            
            // Utiliser ceil() pour les valeurs > 1.0 afin de voir l'effet même sur de petits dégâts
            int newAmount;
            if (multiplier > 1.0f) {
                newAmount = (int)Math.max(1, Math.ceil(amount * multiplier));
            } else {
                newAmount = (int)Math.max(1, Math.round(amount * multiplier));
            }
            
            System.out.println("[ItemStackHurtMixin] Original amount: " + amount + ", Multiplier: " + multiplier + ", Modified amount: " + newAmount);
            return newAmount;
        } else {
            System.out.println("[ItemStackHurtMixin] No DurabilityEffect found in NBT");
        }

        return amount;
    }
}

