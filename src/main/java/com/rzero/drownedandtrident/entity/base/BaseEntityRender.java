package com.rzero.drownedandtrident.entity.base;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public interface BaseEntityRender {
    void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event);
}
