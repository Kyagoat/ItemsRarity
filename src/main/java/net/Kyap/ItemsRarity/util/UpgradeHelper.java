package net.Kyap.ItemsRarity.util;
import net.Kyap.ItemsRarity.item.ModItems;
import net.Kyap.ItemsRarity.util.rarity.data.RarityRatesDataManager;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

public class UpgradeHelper {

    public static boolean isItemUpgradable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        // Vérifier si c'est une pièce d'armure ou arme
        return (item instanceof TieredItem || item instanceof ArmorItem);
    }

    public static boolean hasValidResources(ItemStack resourceStack, ItemStack modMaterialStack) {
        // Vérifier que les stacks ne sont pas vides
        if (resourceStack.isEmpty() || modMaterialStack.isEmpty()) {
            return false;
        }

        // Vérifier que le slot de droite contient une de nos ressources personnalisées
        return isCustomModResource(modMaterialStack);
    }

    /**
     * Vérifie si un item est une ressource de réparation valide pour un item donné
     */
    public static boolean isValidRepairResource(ItemStack gearItem, ItemStack resourceItem) {
        if (gearItem.isEmpty() || resourceItem.isEmpty()) {
            return false;
        }

        // Obtenir l'item original si c'est un item transformé
        Item originalGearItem = gearItem.getItem();
        
        // Obtenir la ressource de réparation de l'item original
        ItemStack[] repairMaterial = getRepairMaterial(originalGearItem);

        resourceItem.getItem();
        for (ItemStack itemStack : repairMaterial) {
            if( Objects.equals(itemStack.getItem(), resourceItem.getItem())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un item est une de nos ressources personnalisées
     */
    public static boolean isCustomModResource(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        Item item = stack.getItem();
        return item == ModItems.BISMUTH.get() || 
               item == ModItems.SULFUR.get() || 
               item == ModItems.FLOURITE.get();
    }

    /**
     * Obtient la ressource de réparation d'un item (basé sur le type d'item)
     * Si l'item n'a pas de ressource de réparation connue, accepte la plume
     */
    private static ItemStack[] getRepairMaterial(Item item) {

        if (item instanceof TieredItem tieredItem) {
            Ingredient ing = tieredItem.getTier().getRepairIngredient();
            if (ing.getItems().length > 0) {
                return ing.getItems();
            }
        }

        if (item instanceof ArmorItem armorItem) {
            Ingredient ing = armorItem.getMaterial().getRepairIngredient();
            if (ing.getItems().length > 0) {
                return ing.getItems();
            }
        }
        return new ItemStack[] { new ItemStack(Items.FEATHER) };
    }

    /**
     * Vérifie si un item a déjà une rareté custom
     */
    public static boolean hasRarity(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("custom_rarity");
    }

    public static ModRarities.ModRarity getRarityFromItem(ItemStack stack) {
        if (stack == null || !stack.hasTag()) return null;
        String rarityId = stack.getOrCreateTag().getString("custom_rarity");
        if (rarityId.isEmpty()) return null;
        
        // Utiliser la méthode centralisée pour éviter la duplication
        return RarityRatesDataManager.getModRarityFromName(rarityId);
    }

    /**
     * Applique une rareté spécifique à un item
     */
    private static void applyRarityToItem(ItemStack stack, ModRarities.ModRarity rarity) {
        stack.getOrCreateTag().putString("custom_rarity", rarity.name().toLowerCase());
    }

    public static boolean upgradeItem(ItemStack stack, ModRarities.ModRarity newRarity) {
        ModRarities.ModRarity currentRarity = getRarityFromItem(stack);
        // Seulement appliquer l'upgrade et reroll les effets si la nouvelle rareté est supérieure ou égale
        if (currentRarity == null || newRarity.getLevel() >= currentRarity.getLevel()) {
            applyRarityToItem(stack, newRarity);
            EffectPoolSystem.rollEffectsOnItem(stack);
            return true; // Upgrade réussi
        }
        return false; // Upgrade échoué - nouvelle rareté inférieure
    }

}