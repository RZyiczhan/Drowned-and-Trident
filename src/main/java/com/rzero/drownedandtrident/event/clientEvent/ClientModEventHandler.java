package com.rzero.drownedandtrident.event.clientEvent;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.modRegistry.menu.ModMenu;
import com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable.TridentEnchantmentScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;


@EventBusSubscriber(modid = DrownedandTrident.MODID, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        // 绑定你的 MenuType 和 Screen
        event.register(ModMenu.TRIDENT_ENCHANTMENT_MENU.get(), TridentEnchantmentScreen::new);
    }
}

