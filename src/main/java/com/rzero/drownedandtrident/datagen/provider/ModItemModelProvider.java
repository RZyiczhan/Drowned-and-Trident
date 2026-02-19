package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.block.DATBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(
                DATBlock.TRIDENT_ENCHANTING_TABLE.getId().getPath(),
                modLoc("block/trident_enchanting_table")
        );
    }
}
