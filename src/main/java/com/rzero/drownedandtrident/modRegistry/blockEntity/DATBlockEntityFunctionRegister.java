package com.rzero.drownedandtrident.modRegistry.blockEntity;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATBlockEntityFunctionRegister {

    public static final DeferredRegister<BlockEntityType<?>> CUSTOM_BLOCK_ENTITY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DrownedandTrident.MODID);

    public static void register(IEventBus eventBus) throws ClassNotFoundException {
        // 初始化Block定义类，保证定义类被使用，来确保其static变量被正常初始化以达成注册
        Class.forName(DATBlockEntity.class.getName());
        CUSTOM_BLOCK_ENTITY.register(eventBus);
    }


}
