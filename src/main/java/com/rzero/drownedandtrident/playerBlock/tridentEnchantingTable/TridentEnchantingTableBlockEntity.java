package com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable;

import com.rzero.drownedandtrident.blockEntity.DATBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TridentEnchantingTableBlockEntity extends EnchantingTableBlockEntity {
    public TridentEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return DATBlockEntity.TRIDENT_ENCHANTING_TABLE_BLOCK_ENTITY.get();
    }
}
