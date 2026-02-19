package com.rzero.drownedandtrident.item;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATItemFunctionRegister {

    public static final DeferredRegister.Items CUSTOM_BLOCK_ITEM =
            DeferredRegister.createItems(DrownedandTrident.MODID);

    public static void register(IEventBus eventBus){
        new DATItem();
        CUSTOM_BLOCK_ITEM.register(eventBus);
    }

}
