package net.Kyap.ItemsRarity.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.Kyap.ItemsRarity.block.entity.custom.EnhancedAnvilBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EnhancedBlockEntityRenderer implements BlockEntityRenderer<EnhancedAnvilBlockEntity> {

    public EnhancedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Constructeur vide
    }

    @Override
    public void render(EnhancedAnvilBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        
        // Slot 0: Resource Item (gauche)
        ItemStack resourceItem = pBlockEntity.getItemHandler().getStackInSlot(0);
        if (!resourceItem.isEmpty()) {
            renderItem(resourceItem, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, itemRenderer,
                    0.35f, 1.08f, 0.55f, 1f);
        }

        // Slot 1: Gear Item (centre)
        ItemStack gearItem = pBlockEntity.getItemHandler().getStackInSlot(1);
        if (!gearItem.isEmpty()) {
            renderItem(gearItem, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, itemRenderer,
                    0.5f, 1.027f, 0.2f, 2f);
        }

        // Slot 2: Mod Material Item (droite)
        ItemStack modMaterialItem = pBlockEntity.getItemHandler().getStackInSlot(2);
        if (!modMaterialItem.isEmpty()) {
            renderItem(modMaterialItem, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, itemRenderer,
                    0.76f, 1.08f, 0.3f, 1f);
        }
    }

    private void renderItem(ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer,
                                       int packedLight, int packedOverlay, ItemRenderer itemRenderer,
                                       float x, float y, float z, float scale) {
        poseStack.pushPose();

        // Position
        poseStack.translate(x, y, z);

        // Rotation pour poser à plat
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

        // Échelle personnalisée
        poseStack.scale(scale, scale, scale);

        // Rendu
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, null, 0);

        poseStack.popPose();
    }

}
