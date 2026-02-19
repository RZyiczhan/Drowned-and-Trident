package com.rzero.drownedandtrident.item;

import com.rzero.drownedandtrident.block.DATBlock;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;

public class DATItem {

    public static final DeferredItem<BlockItem> TRIDENT_ENCHANTING_TABLE =
            DATItemFunctionRegister.CUSTOM_BLOCK_ITEM.registerSimpleBlockItem(DATBlock.TRIDENT_ENCHANTING_TABLE);

}

