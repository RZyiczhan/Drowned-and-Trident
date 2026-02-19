package com.rzero.drownedandtrident.playerBlock.dataGen;

import com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable.TridentEnchantingTableDataGen;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ModBlockDataGenRegister {


    private static List<BaseItemModelAppearance> customItemModelList = new ArrayList<>();
    static {
        customItemModelList.add(new TridentEnchantingTableDataGen());
    }

    private static List<BaseBlockStateAppearance> customBlockStateList = new ArrayList<>();
    static {
        customBlockStateList.add(new TridentEnchantingTableDataGen());
    }


    public static void registerStatesAndModels(BlockStateProvider provider) {
        for (BaseBlockStateAppearance blockState : customBlockStateList){
            blockState.blockStateAppearanceGenerator(provider);
        }
    }

    public static void registerModels(ItemModelProvider provider){
        for (BaseItemModelAppearance itemModel : customItemModelList){
            itemModel.itemModelAppearanceGenerator(provider);
        }
    }

}
