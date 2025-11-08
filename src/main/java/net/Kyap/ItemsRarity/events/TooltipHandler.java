package net.Kyap.ItemsRarity.events;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.effects.EffectRegistry;
import net.Kyap.ItemsRarity.util.effects.GearEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        
        if (!stack.hasTag()) return;
        
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("CustomEffects")) return;
        
        ListTag effectsList = tag.getList("CustomEffects", 8); // 8 = StringTag
        
        // Chercher si l'effet damage existe
        String damageEffectTooltip = null;
        GearEffect damageEffect = EffectRegistry.getEffect("damage");
        if (damageEffect != null && EffectRegistry.hasEffectApplied(stack, "damage")) {
            float effectValue = damageEffect.getValueByRarity(stack);
            if (effectValue != 0) {
                damageEffectTooltip = damageEffect.getTooltip(effectValue);
            }
        }
        
        // Parcourir le tooltip pour insérer l'effet damage à la bonne place
        List<Component> tooltip = event.getToolTip();
        if (damageEffectTooltip != null) {
            for (int i = 0; i < tooltip.size(); i++) {
                String line = tooltip.get(i).getString();
                // Chercher la ligne contenant "Attack Damage" (en anglais ou traduit)
                if (line.contains("Attack Damage") || line.contains("Dégâts d'attaque") || line.toLowerCase().contains("attack") || line.toLowerCase().contains("damage")) {
                    // Obtenir le component existant
                    Component originalComponent = tooltip.get(i);

                    // Copie le component original (pour préserver le style vert natif) puis ajoute le tooltip de damage
                    MutableComponent newComponent;
                    if (originalComponent instanceof MutableComponent) {
                        newComponent = ((MutableComponent) originalComponent).copy();
                    } else {
                        newComponent = Component.literal(originalComponent.getString());
                    }

                    float effectValue = damageEffect.getValueByRarity(stack);
                    ChatFormatting formatting = effectValue >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED;
                    newComponent.append(Component.literal(" " + damageEffectTooltip).withStyle(formatting));

                    // Remplacer la ligne par la copie stylée
                    tooltip.set(i, newComponent);
                    break;
                }
            }
        }
        
        // Afficher les autres effets normalement (sauf damage qui est déjà géré)
        for (int i = 0; i < effectsList.size(); i++) {
            String effectId = effectsList.getString(i);
            
            // Skip damage effect car il est déjà ajouté à la ligne de dégâts
            if (effectId.equals("damage")) continue;
            
            GearEffect effect = EffectRegistry.getEffect(effectId);
            
            if (effect != null) {
                float effectValue = effect.getValueByRarity(stack);
                // Afficher le tooltip pour toutes les valeurs non-nulles (positives et négatives)
                if (effectValue != 0) {
                    String tooltipText = effect.getTooltip(effectValue);
                    event.getToolTip().add(Component.literal("§r" + tooltipText));
                }
            }
        }
    }
}
