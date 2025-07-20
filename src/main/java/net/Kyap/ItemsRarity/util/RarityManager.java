package net.Kyap.ItemsRarity.util;
import net.Kyap.ItemsRarity.item.ModItems;
import net.Kyap.ItemsRarity.item.ItemTransformationManager;
import net.minecraft.world.item.*;

public class RarityManager {

    public static boolean isItemUpgradable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        // Obtenir l'item original si c'est un item transformé
        Item item = ItemTransformationManager.getOriginalItem(stack);
        
        // Vérifier si c'est une arme
        if (item instanceof SwordItem || 
            item instanceof BowItem) {
            return true;
        }
        
        // Vérifier si c'est un outil
        if (item instanceof AxeItem || 
            item instanceof PickaxeItem || 
            item instanceof ShovelItem || 
            item instanceof HoeItem) {
            return true;
        }
        
        // Vérifier si c'est une pièce d'armure
        if (item instanceof ArmorItem) {
            return true;
        }
        
        // Vérifier si c'est un item avec durabilité (comme les cisailles, briquet, etc.)
        if (stack.isDamageableItem() && stack.getMaxDamage() > 0) {
            return true;
        }
        
        return false; // Pas un équipement upgradable
    }

    /**
     * Converts a Minecraft Rarity to ModRarity enum
     */
    private static ModRarities.ModRarity getModRarityFromMinecraft(Rarity rarity) {
        for (ModRarities.ModRarity modRarity : ModRarities.ModRarity.values()) {
            if (modRarity.getMinecraftRarity().equals(rarity)) {
                return modRarity;
            }
        }
        return null; // Rarity not found in our enum
    }

    public static boolean hasValidResources(ItemStack resourceStack, ItemStack modMaterialStack) {
        // Vérifier que les stacks ne sont pas vides
        if (resourceStack.isEmpty() || modMaterialStack.isEmpty()) {
            return false;
        }

        // Vérifier que le slot de droite contient une de nos ressources personnalisées
        return isCustomModResource(modMaterialStack);
    }

    // 💡 Suggestion : méthode combinée pour ton BlockEntity
    public static boolean canEnhance(ItemStack gear, ItemStack resource, ItemStack modMaterial) {
        return isItemUpgradable(gear) && hasValidResources(resource, modMaterial);
    }

    /**
     * Vérifie si un item est une ressource de réparation valide pour un item donné
     * Si l'item n'a pas de ressource de réparation connue, accepte la plume
     */
    public static boolean isValidRepairResource(ItemStack gearItem, ItemStack resourceItem) {
        if (gearItem.isEmpty() || resourceItem.isEmpty()) {
            return false;
        }

        // Obtenir l'item original si c'est un item transformé
        Item originalGearItem = ItemTransformationManager.getOriginalItem(gearItem);
        
        // Obtenir la ressource de réparation de l'item original
        Item repairMaterial = getRepairMaterial(originalGearItem);
        
        if (repairMaterial != null) {
            // L'item a une ressource de réparation connue
            return resourceItem.getItem() == repairMaterial;
        } else {
            // L'item n'a pas de ressource connue, accepter la plume
            return resourceItem.getItem() == Items.FEATHER;
        }
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
     */
    private static Item getRepairMaterial(Item item) {
        // Pour les outils en bois
        if (item == Items.WOODEN_SWORD || item == Items.WOODEN_AXE || 
            item == Items.WOODEN_PICKAXE || item == Items.WOODEN_SHOVEL || 
            item == Items.WOODEN_HOE) {
            return Items.OAK_PLANKS;
        }
        
        // Pour les outils en pierre
        if (item == Items.STONE_SWORD || item == Items.STONE_AXE || 
            item == Items.STONE_PICKAXE || item == Items.STONE_SHOVEL || 
            item == Items.STONE_HOE) {
            return Items.COBBLESTONE;
        }
        
        // Pour les outils/armures en fer
        if (item == Items.IRON_SWORD || item == Items.IRON_AXE || 
            item == Items.IRON_PICKAXE || item == Items.IRON_SHOVEL || 
            item == Items.IRON_HOE || item == Items.IRON_HELMET ||
            item == Items.IRON_CHESTPLATE || item == Items.IRON_LEGGINGS ||
            item == Items.IRON_BOOTS) {
            return Items.IRON_INGOT;
        }
        
        // Pour les outils/armures en diamant
        if (item == Items.DIAMOND_SWORD || item == Items.DIAMOND_AXE || 
            item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_SHOVEL || 
            item == Items.DIAMOND_HOE || item == Items.DIAMOND_HELMET ||
            item == Items.DIAMOND_CHESTPLATE || item == Items.DIAMOND_LEGGINGS ||
            item == Items.DIAMOND_BOOTS) {
            return Items.DIAMOND;
        }
        
        // Pour les outils/armures en netherite
        if (item == Items.NETHERITE_SWORD || item == Items.NETHERITE_AXE || 
            item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_SHOVEL || 
            item == Items.NETHERITE_HOE || item == Items.NETHERITE_HELMET ||
            item == Items.NETHERITE_CHESTPLATE || item == Items.NETHERITE_LEGGINGS ||
            item == Items.NETHERITE_BOOTS) {
            return Items.NETHERITE_INGOT;
        }

        
        // Pour les outils/armures en or
        if (item == Items.GOLDEN_SWORD || item == Items.GOLDEN_AXE || 
            item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_SHOVEL || 
            item == Items.GOLDEN_HOE || item == Items.GOLDEN_HELMET ||
            item == Items.GOLDEN_CHESTPLATE || item == Items.GOLDEN_LEGGINGS ||
            item == Items.GOLDEN_BOOTS) {
            return Items.GOLD_INGOT;
        }
        
        // Pour les armures en cuir
        if (item == Items.LEATHER_HELMET || item == Items.LEATHER_CHESTPLATE ||
            item == Items.LEATHER_LEGGINGS || item == Items.LEATHER_BOOTS) {
            return Items.LEATHER;
        }
        
        // Pour les armures en mailles
        if (item == Items.CHAINMAIL_HELMET || item == Items.CHAINMAIL_CHESTPLATE ||
            item == Items.CHAINMAIL_LEGGINGS || item == Items.CHAINMAIL_BOOTS) {
            return Items.IRON_INGOT; // Les mailles utilisent du fer
        }
        
        // Pour l'arc
        if (item == Items.BOW) {
            return Items.STRING;
        }
        
        // Pour l'arbalète
        if (item == Items.CROSSBOW) {
            return Items.STICK;
        }
        
        // Pour le trident
        if (item == Items.TRIDENT) {
            return Items.PRISMARINE_SHARD;
        }
        
        // Retourne null si aucune ressource de réparation n'est trouvée
        return null;
    }
}