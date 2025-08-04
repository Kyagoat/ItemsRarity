package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
}