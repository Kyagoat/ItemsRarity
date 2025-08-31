package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID)
public class GearEffectHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            
            // Utiliser le nouveau système centralisé
            EffectRegistry.handleOnHitEffects(attacker, event.getEntity(), weapon, event);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // Nettoyer d'abord tous les modificateurs Momentum, ils seront réappliqués si nécessaire
        EffectRegistry.cleanupMomentumEffect(player);
        
        // Parcours tous les slots d'armure et réapplique les effets actifs
        for (ItemStack armorPiece : player.getArmorSlots()) {
            // Vérifie et applique tous les effets on_tick sur cette pièce d'armure
            EffectRegistry.handleOnTickEffects(player, armorPiece);
        }
    }
}