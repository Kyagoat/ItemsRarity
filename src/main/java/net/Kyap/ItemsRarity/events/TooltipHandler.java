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

        ListTag effectsList = tag.getList("CustomEffects", 8);

        for (int i = 0; i < effectsList.size(); i++) {
            String effectId = effectsList.getString(i);

            if (isAttributeEffect(effectId)) {
                continue;
            }

            GearEffect effect = EffectRegistry.getEffect(effectId);

            if (effect != null) {
                float effectValue = effect.getValueByRarity(stack);
                if (effectValue != 0) {
                    String tooltipText = effect.getTooltip(effectValue);
                    event.getToolTip().add(Component.literal("§7" + tooltipText));
                }
            }
        }
    }

    private static boolean isAttributeEffect(String effectId) {
        return switch (effectId) {
            case "crit_chance",
                 "damage",
                 "health",
                 "speed",
                 "crit_damage",
                 "armor_penetration",
                 "armor_pierce",
                 "life_steal",
                 "dodge",
                 "arrow_damage",
                 "overheal",
                 "mining_speed",
                 "reach"
                    -> true;
            default -> false;
        };
    }
}