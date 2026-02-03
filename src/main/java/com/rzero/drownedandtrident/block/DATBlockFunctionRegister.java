package com.rzero.drownedandtrident.block;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATBlockFunctionRegister {

    public static final DeferredRegister.Blocks CUSTOM_BLOCK =
            DeferredRegister.createBlocks(DrownedandTrident.MODID);

    public static void register(IEventBus eventBus){
        CUSTOM_BLOCK.register(eventBus);
    }

}
