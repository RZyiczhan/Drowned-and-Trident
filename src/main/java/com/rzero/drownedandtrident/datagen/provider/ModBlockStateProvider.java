package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.block.DATBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Block tridentTable = DATBlock.TRIDENT_ENCHANTING_TABLE.get();

        // 创建模型：继承自原版 minecraft:block/enchanting_table，但替换材质
        // 注意：texture 的 key ("top", "bottom", "side") 取决于父模型定义的变量
        ModelFile model = models().withExistingParent(
                        "trident_enchanting_table", // 生成的模型文件名
                        "minecraft:block/enchanting_table" // 父模型
                )
//                .texture("top", mcLoc("block/enchanting_table_top"))      // 你的贴图路径
//                .texture("bottom", mcLoc("block/enchanting_table_bottom"))
//                .texture("side", mcLoc("block/enchanting_table_side"))
//                // 原版附魔台还需要一个粒子材质
//                .texture("particle", mcLoc("block/enchanting_table_top"));
        ;
        // 注册 BlockState：无论什么状态，都使用上面定义的这个模型
        simpleBlock(tridentTable, model);
    }
}
