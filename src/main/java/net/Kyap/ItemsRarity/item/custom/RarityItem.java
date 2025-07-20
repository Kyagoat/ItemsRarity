package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Classe de base pour tous les items avec rareté personnalisée
 */
public abstract class RarityItem extends Item {
    
    private final ModRarities.ModRarity customRarity;
    private final Item originalItem;
    
    public RarityItem(Item originalItem, ModRarities.ModRarity rarity) {
        super(new Properties()
                .stacksTo(originalItem.getMaxStackSize())
                .durability(originalItem.getMaxDamage())
                .rarity(rarity.getMinecraftRarity())
        );
        this.customRarity = rarity;
        this.originalItem = originalItem;
    }
    
    @Override
    public Rarity getRarity(ItemStack pStack) {
        return customRarity.getMinecraftRarity();
    }
    
    public ModRarities.ModRarity getCustomRarity() {
        return customRarity;
    }
    
    public Item getOriginalItem() {
        return originalItem;
    }
    
    @Override
    public boolean isDamageable(ItemStack stack) {
        return originalItem.isDamageable(ItemStack.EMPTY);
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return originalItem.getMaxDamage(ItemStack.EMPTY);
    }
    
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return originalItem.isValidRepairItem(pToRepair, pRepair);
    }
}
