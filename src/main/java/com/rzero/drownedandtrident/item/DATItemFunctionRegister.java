package com.rzero.drownedandtrident.item;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATItemFunctionRegister {

//    public static final DeferredRegister<Item> OVERRIDE_ITEM =
//            DeferredRegister.create(BuiltInRegistries.ITEM, DrownedAndTridentMod.MODID);

    public static final DeferredRegister.Items OVERRIDE_ITEM =
            DeferredRegister.createItems(DrownedandTrident.MODID);

    public static final DeferredItem<Item> DAT_TRIDENT_ITEM =
            OVERRIDE_ITEM.register("dat_trident_item", () -> new Item(new Item.Properties()) );

    public static void register(IEventBus eventBus){
        OVERRIDE_ITEM.register(eventBus);
    }

}
