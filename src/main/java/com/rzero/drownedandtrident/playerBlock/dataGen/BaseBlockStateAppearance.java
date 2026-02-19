package com.rzero.drownedandtrident.playerBlock.dataGen;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

/**
 * 每个BlockState必须实现的接口，用于接入datagen，来给Block添加texture
 */
public interface BaseBlockStateAppearance {

    void blockStateAppearanceGenerator(BlockStateProvider provider);
}
