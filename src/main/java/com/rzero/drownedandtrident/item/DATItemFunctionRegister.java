package com.rzero.drownedandtrident.item;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.item.override.DATTridentItem.DATTridentItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TridentItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DATItemFunctionRegister {

    public static final DeferredRegister.Items OVERRIDE_ITEM =
            DeferredRegister.createItems(DrownedandTrident.MODID);

    public static final DeferredItem<Item> DAT_TRIDENT_ITEM =
            OVERRIDE_ITEM.register("dat_trident_item", () -> new DATTridentItem(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .durability(250)
                            .attributes(TridentItem.createAttributes())
                            .component(DataComponents.TOOL, TridentItem.createToolProperties())
                            .component(TridentDataComponentRegister.FAN_SPLIT_ANGLE, 10)
                            .component(TridentDataComponentRegister.FAN_SPLIT_TICK, 0)
            ));

    public static void register(IEventBus eventBus){
        OVERRIDE_ITEM.register(eventBus);
    }

}
