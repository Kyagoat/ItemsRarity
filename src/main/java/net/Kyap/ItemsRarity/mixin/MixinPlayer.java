package net.Kyap.ItemsRarity.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class MixinPlayer {

    @ModifyVariable(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("STORE"), ordinal = 2)
    private boolean disableVanillaCrit(boolean isCritical) {
        return false;
    }
}