package com.rzero.drownedandtrident.enchantment.base;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 每个自定义附魔必须实现的接口，用于接入datagen
 */
public interface BaseEnchantmentDefinition {

    /**
     * 每个自定义附魔必须实现的datagen方法
     * @param context
     */
    public void bootstrap(BootstrapContext<Enchantment> context);

}
