package net.Kyap.ItemsRarity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin {

    /**
     * Intercepte l'appel à ForgeHooks.getCriticalHit() et remplace la logique
     */
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;getCriticalHit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;ZF)Lnet/minecraftforge/event/entity/player/CriticalHitEvent;"))
    private CriticalHitEvent redirectCriticalHit(Player player, Entity target, boolean vanillaCrit, float damageModifier) {
        boolean customCrit = calculateCustomCrit(player);
        if (customCrit) {
            return ForgeHooks.getCriticalHit(player, target, true, 1.5F);
        }
        return null;
    }

    private boolean calculateCustomCrit(Player player) {
        float baseCritChance = 0.0f;
        float totalCritChance;
        // Ajouter le bonus de l'arme
        ItemStack weapon = player.getMainHandItem();
        if (weapon.hasTag()) {
            CompoundTag tag = weapon.getTag();
            if (tag != null && tag.contains("CritChanceEffect")) {
                float bonusCritChance = tag.getFloat("CritChanceEffect");
                baseCritChance += bonusCritChance;
            }
        }
        totalCritChance = Math.max(0.0f, Math.min(baseCritChance, 1.0f));

        return player.level().random.nextFloat() < totalCritChance;
    }
}