package com.rzero.drownedandtrident.enchantment.base;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantment;

public interface BaseEnchantmentDefinition {

    public void bootstrap(BootstrapContext<Enchantment> context);

}
