package net.Kyap.ItemsRarity.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.item.ModItems;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnhancedAnvilBlockScreen extends AbstractContainerScreen<EnhancedAnvilBlockMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ItemsRarity.MOD_ID, "textures/gui/enhanced_anvil.png");

    public EnhancedAnvilBlockScreen(EnhancedAnvilBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        ItemStack leftGhost = getLeftGhostItem();
        if (!leftGhost.isEmpty()) {
            renderGrayGhostItem(guiGraphics, x + 27, y + 47, leftGhost);
        }

        ItemStack rightGhost = getRightGhostItem();
        if (!rightGhost.isEmpty()) {
            renderGrayGhostItem(guiGraphics, x + 134, y + 47, rightGhost);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderGhostTooltips(guiGraphics, mouseX, mouseY);
    }

    private void renderGhostTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isHovering(27, 47, 16, 16, mouseX, mouseY)) {
            ItemStack stack = getLeftGhostItem();
            if (!stack.isEmpty()) {
                guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
            }
        }

        if (isHovering(134, 47, 16, 16, mouseX, mouseY)) {
            ItemStack stack = getRightGhostItem();
            if (!stack.isEmpty()) {
                guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
            }
        }
    }

    private ItemStack getLeftGhostItem() {
        ItemStack materialStack = this.menu.getSlot(36).getItem();
        ItemStack gearStack = this.menu.getSlot(37).getItem();

        if (!gearStack.isEmpty() && materialStack.isEmpty()) {
            ItemStack[] repairMaterials = UpgradeHelper.getRepairMaterial(gearStack.getItem());
            if (repairMaterials.length > 0) {
                int index = (int) ((System.currentTimeMillis() / 1000) % repairMaterials.length);
                return repairMaterials[index];
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getRightGhostItem() {
        ItemStack modStack = this.menu.getSlot(38).getItem();

        if (modStack.isEmpty()) {
            ItemStack[] modMaterials = {
                    new ItemStack(ModItems.SULFUR.get()),
                    new ItemStack(ModItems.BISMUTH.get()),
                    new ItemStack(ModItems.FLOURITE.get())
            };
            int index = (int) ((System.currentTimeMillis() / 1000) % modMaterials.length);
            return modMaterials[index];
        }
        return ItemStack.EMPTY;
    }

    /**
     * Affiche un item avec un voile gris PAR-DESSUS.
     */
    private void renderGrayGhostItem(GuiGraphics guiGraphics, int x, int y, ItemStack stack) {
        guiGraphics.renderFakeItem(stack, x, y);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
        guiGraphics.fill(x, y, x + 16, y + 16, 0x90404040);
        guiGraphics.pose().popPose();
    }
}