package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.playerBlock.dataGen.ModBlockDataGenRegister;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlockDataGenRegister.registerStatesAndModels(this);
    }
}
