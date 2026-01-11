package com.rzero.drownedandtrident.event.render;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import com.rzero.drownedandtrident.item.override.DATTridentItem.CustomTridentRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class RenderEventsHandler {

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            // 懒加载渲染器
            private CustomTridentRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new CustomTridentRenderer();
                }
                return renderer;
            }
        }, DATItemFunctionRegister.DAT_TRIDENT_ITEM.get()); // 填入你的物品 RegistryObject
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    DATItemFunctionRegister.DAT_TRIDENT_ITEM.get(),
                    ResourceLocation.withDefaultNamespace("throwing"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );
        });
    }
}
