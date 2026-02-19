package com.rzero.drownedandtrident.modRegistry.block;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATBlockFunctionRegister {

    public static final DeferredRegister.Blocks CUSTOM_BLOCK =
            DeferredRegister.createBlocks(DrownedandTrident.MODID);

    public static void register(IEventBus eventBus) throws ClassNotFoundException {
        // 初始化Block定义类，保证定义类被使用，来确保其static变量被正常初始化以达成注册
        Class.forName(DATBlock.class.getName());
        CUSTOM_BLOCK.register(eventBus);
    }

}
