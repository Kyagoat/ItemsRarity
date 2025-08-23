package net.Kyap.ItemsRarity.events;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.effects.EffectRegistry;
import net.Kyap.ItemsRarity.util.effects.GearEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        
        if (!stack.hasTag()) return;
        
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("CustomEffects")) return;
        
        ListTag effectsList = tag.getList("CustomEffects", 8); // 8 = StringTag
        
        for (int i = 0; i < effectsList.size(); i++) {
            String effectId = effectsList.getString(i);
            GearEffect effect = EffectRegistry.getEffect(effectId);
            
            if (effect != null) {
                float effectValue = effect.getValueByRarity(stack);
                // Afficher le tooltip pour toutes les valeurs non-nulles (positives et négatives)
                if (effectValue != 0) {
                    String tooltipText = effect.getTooltip(effectValue, stack);
                    event.getToolTip().add(Component.literal("§r" + tooltipText));
                }
            }
        }
    }
}
