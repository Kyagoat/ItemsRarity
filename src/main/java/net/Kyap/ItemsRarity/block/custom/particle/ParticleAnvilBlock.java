package net.Kyap.ItemsRarity.block.custom.particle;

import net.Kyap.ItemsRarity.block.entity.custom.EnhancedAnvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ParticleAnvilBlock {

    /**
     * Creates particles around the anvil as if the item is being struck
     */
    public static void spawnRarityParticles(Level level, BlockPos pos, EnhancedAnvilBlockEntity anvil) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack gearItem = anvil.getItemHandler().getStackInSlot(1);
        if (gearItem.isEmpty()) {
            return;
        }

        Vec3 particleColor = getParticleColorForRarity(gearItem);

        int particleMultiplier = getParticleMultiplierForRarity(gearItem);

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.0;
        double centerZ = pos.getZ() + 0.5;

        RandomSource random = level.getRandom();

        for (int i = 0; i < 15 * particleMultiplier; i++) {
            double velocityX = (random.nextDouble() - 0.5) * 0.4;
            double velocityY = random.nextDouble() * 0.3 + 0.1;
            double velocityZ = (random.nextDouble() - 0.5) * 0.4;

            double startX = centerX + (random.nextDouble() - 0.5) * 0.3;
            double startY = centerY + random.nextDouble() * 0.1;
            double startZ = centerZ + (random.nextDouble() - 0.5) * 0.3;

            DustParticleOptions dustOptions = new DustParticleOptions(
                    new Vector3f(
                            (float) particleColor.x,
                            (float) particleColor.y,
                            (float) particleColor.z
                    ),
                    0.5f
            );

            serverLevel.sendParticles(
                    dustOptions,
                    startX, startY, startZ,
                    1, // count
                    velocityX, velocityY, velocityZ,
                    0.2
            );
        }

        for (int i = 0; i < 6 * particleMultiplier; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.6;
            double offsetY = random.nextDouble() * 0.2;
            double offsetZ = (random.nextDouble() - 0.5) * 0.6;

            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                    1,
                    (random.nextDouble() - 0.5) * 0.3,
                    random.nextDouble() * 0.2 + 0.1,
                    (random.nextDouble() - 0.5) * 0.3,
                    0.15
            );
        }
    }

    /**
     * Determines the color of the particles based on the rarity of the item
     */
    private static Vec3 getParticleColorForRarity(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("custom_rarity")) {
            String customRarityName = stack.getTag().getString("custom_rarity");
            return switch (customRarityName) {
                case "common" -> new Vec3(0.6, 0.6, 0.6);
                case "uncommon" -> new Vec3(0.2, 0.8, 0.2);
                case "rare" -> new Vec3(0.2, 0.2, 1.0);
                case "epic" -> new Vec3(1.0, 0.0, 1.0);
                case "legendary" -> new Vec3(1.0, 0.5, 0.0);
                case "mythic" -> new Vec3(1.0, 0.0, 0.0);
                default -> throw new IllegalStateException("Unexpected value: " + customRarityName);
            };
        }
        Rarity rarity = stack.getRarity();
        return switch (rarity) {
            case COMMON -> new Vec3(0.6, 0.6, 0.6);      // Gray
            case UNCOMMON -> new Vec3(0.2, 0.8, 0.2);    // Green
            case RARE -> new Vec3(0.2, 0.2, 1.0);        // Blue
            case EPIC -> new Vec3(1.0, 0.0, 1.0);        // Purple/Magenta
        };
    }

    /**
     * Determines the particle multiplier based on the rarity of the item
     * The higher the rarity, the more particles are generated
     */
    private static int getParticleMultiplierForRarity(ItemStack stack) {
        // Verify if the item has a custom rarity tag
        if (stack.hasTag() && stack.getTag().contains("custom_rarity")) {
            String customRarityName = stack.getTag().getString("custom_rarity");
            return switch (customRarityName) {
                case "common", "uncommon" -> 1;
                case "rare" -> 2;
                case "epic" -> 5;
                case "legendary" -> 7;
                case "mythic" -> 10;
                default -> throw new IllegalStateException("Unexpected value: " + customRarityName);
            };
        }

        // Otherwise, use the default rarity system
        Rarity rarity = stack.getRarity();
        return switch (rarity) {
            case COMMON -> 1;
            case UNCOMMON -> 1;
            case RARE, EPIC -> 2;
        };
    }
}