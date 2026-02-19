package com.rzero.drownedandtrident.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * 模组Block定义注册类
 * 从DATBlockFunctionRegister拆出来的原因是不想把注册功能和Block定义放在同一个地方
 * 使用的时候用DATBlock获得模组Block定义也比用DATBlockFunctionRegister获取来得可读性高一点
 */
public class DATBlock {

    // block属性copy自原版附魔台
    public static final DeferredBlock<Block> TRIDENT_ENCHANTING_TABLE =
            DATBlockFunctionRegister.CUSTOM_BLOCK.register("trident_enchanting_table",
                    () -> new TridentEnchantingTableBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .lightLevel(p_152692_ -> 7)
                            .strength(5.0F, 1200.0F)));

}
