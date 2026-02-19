package com.rzero.drownedandtrident.playerBlock.dataGen;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

/**
 * 每个玩家可获得可手持的物品/方块必须实现的接口，用于接入datagen，来给item添加模型以展示在世界和物品栏UI
 */
public interface BaseItemModelAppearance {

    void itemModelAppearanceGenerator(ItemModelProvider provider);
}
