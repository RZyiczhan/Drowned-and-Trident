package com.rzero.drownedandtrident.blockEntity;

import com.rzero.drownedandtrident.block.DATBlock;
import com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable.TridentEnchantingTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DATBlockEntity {

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TridentEnchantingTableBlockEntity>> TRIDENT_ENCHANTING_TABLE_BLOCK_ENTITY =
            DATBlockEntityFunctionRegister.CUSTOM_BLOCK_ENTITY.register("trident_enchanting_table", () ->
                    BlockEntityType.Builder.of(
                            TridentEnchantingTableBlockEntity::new,
                            DATBlock.TRIDENT_ENCHANTING_TABLE.get()
                    ).build(null)
            );

}
