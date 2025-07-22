package net.Kyap.ItemsRarity.util;
import net.Kyap.ItemsRarity.item.ModItems;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RarityManager {

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
     * Fait un roll pour déterminer le nouveau tier selon la ressource utilisée
     * Plus le tier est élevé, plus c'est rare à obtenir
     */

    public static String rollNewTier(ItemStack resourceStack) {
        String itemId = resourceStack.getItem().getDescriptionId();
        double roll = ThreadLocalRandom.current().nextDouble();

        if (itemId.contains("bismuth")) {
            if (roll < 0.40) return "rare";
            else if (roll < 0.75) return "epic";
            else if (roll < 0.95) return "legendary";
            else return "mythic";
        } else if (itemId.contains("sulfur")) {
            if (roll < 0.50) return "uncommon";
            else if (roll < 0.80) return "rare";
            else if (roll < 0.95) return "epic";
            else return "legendary";
        } else if (itemId.contains("flourite")) {
            if (roll < 0.70) return "uncommon";
            else if (roll < 0.95) return "rare";
            else return "epic";
        }
        return null; // échec du roll
    }
}