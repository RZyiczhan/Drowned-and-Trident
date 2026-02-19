package com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable;

import com.rzero.drownedandtrident.modRegistry.block.DATBlock;
import com.rzero.drownedandtrident.playerBlock.dataGen.BaseBlockStateAppearance;
import com.rzero.drownedandtrident.playerBlock.dataGen.BaseItemModelAppearance;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class TridentEnchantingTableDataGen implements BaseBlockStateAppearance, BaseItemModelAppearance {

    @Override
    public void blockStateAppearanceGenerator(BlockStateProvider provider) {
        Block tridentTable = DATBlock.TRIDENT_ENCHANTING_TABLE.get();

        // 创建模型：继承自原版 minecraft:block/enchanting_table，但替换材质
        // 注意：texture 的 key ("top", "bottom", "side") 取决于父模型定义的变量
        ModelFile model = provider.models().withExistingParent(
                "trident_enchanting_table", // 生成的模型文件名
                "minecraft:block/enchanting_table" // 父模型
        )
                .texture("top", provider.modLoc("block/trident_enchanting_table/trident_enchanting_table_top"))      // 你的贴图路径
//                .texture("bottom", mcLoc("block/enchanting_table_bottom"))
//                .texture("side", mcLoc("block/enchanting_table_side"))
//                // 原版附魔台还需要一个粒子材质
//                .texture("particle", mcLoc("block/enchanting_table_top"));
                ;
        // 注册 BlockState：无论什么状态，都使用上面定义的这个模型
        provider.simpleBlock(tridentTable, model);
    }

    @Override
    public void itemModelAppearanceGenerator(ItemModelProvider provider) {
        provider.withExistingParent(
                DATBlock.TRIDENT_ENCHANTING_TABLE.getId().getPath(),
                provider.modLoc("block/trident_enchanting_table")
        );
    }
}
