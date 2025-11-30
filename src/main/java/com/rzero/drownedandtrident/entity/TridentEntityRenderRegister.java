package com.rzero.drownedandtrident.entity;

import com.rzero.drownedandtrident.entity.base.BaseEntityRender;
import com.rzero.drownedandtrident.entity.override.AttackerProtectLightning.AttackerProtectLigntningRender;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTridentRender;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义Entity渲染器的注册器
 */
public class TridentEntityRenderRegister {

    private static List<BaseEntityRender> renderList = new ArrayList<>();

    static {
        renderList.add(new AttackerProtectLigntningRender());
        renderList.add(new DATThrownTridentRender());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (BaseEntityRender render : renderList){
            render.onRegisterRenderers(event);
        }
    }

}
