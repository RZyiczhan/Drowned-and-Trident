package com.rzero.drownedandtrident.entity.override.DATThrownTrident;

import com.rzero.drownedandtrident.entity.TridentEntityFunctionRegister;
import com.rzero.drownedandtrident.entity.base.BaseEntityRender;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class DATThrownTridentRender implements BaseEntityRender {

    @Override
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                TridentEntityFunctionRegister.DAT_THROWN_TRIDENT.get(),
                ThrownTridentRenderer::new
        );
    }
}
