package com.rzero.drownedandtrident.entity.override.AttackerProtectLightning;

import com.rzero.drownedandtrident.entity.TridentEntityFunctionRegister;
import com.rzero.drownedandtrident.entity.base.BaseEntityRender;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 *  自定义闪电的渲染器
 */
public class AttackerProtectLigntningRender implements BaseEntityRender {

    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 使用原版渲染器渲染自定义闪电
        event.registerEntityRenderer(
                TridentEntityFunctionRegister.ATTACK_PROTECT_LIGHTNING.get(),
                LightningBoltRenderer::new
        );
    }

}
