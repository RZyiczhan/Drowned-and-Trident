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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class RenderEventsHandler {

    private static final Logger log = LoggerFactory.getLogger(RenderEventsHandler.class);

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
        log.debug("been called");
        event.enqueueWork(() -> {
            // 注册属性，确保 JSON 里的 "throwing": 1 能被检测到
            ItemProperties.register(
                    DATItemFunctionRegister.DAT_TRIDENT_ITEM.get(),
                    ResourceLocation.withDefaultNamespace("throwing"), // 对应 minecraft:throwing
                    (stack, level, entity, seed) ->
//                            1F
                            entity != null && entity.isUsingItem() && entity.getUseItem().is(stack.getItem()) ? 1.0F : 0.0F
            );
        });
    }
}
