package net.Kyap.ItemsRarity.datagen;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, ItemsRarity.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Enhanced anvil block uses custom model files, so we skip auto-generation
        // blockWithItem(ModBlocks.ENHANCED_ANVIL_BLOCK);
    }
        // This method can be used to add block tags if needed in the future
}
