package com.rzero.drownedandtrident.enchantment;

import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.enchantment.custom.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantmentBootstrapper implements RegistrySetBuilder.RegistryBootstrap<Enchantment> {

    public static List<BaseEnchantmentDefinition> customEnchantmentList = new ArrayList<>();

    static {
        customEnchantmentList.add(new ThunderStormEnchantment());
        customEnchantmentList.add(new ErosionEnchantment());
        customEnchantmentList.add(new FanSplitEnchantment());
        customEnchantmentList.add(new ExplosiveShootEnchantment());
        customEnchantmentList.add(new ThunderTrajectoryEnchantment());
        customEnchantmentList.add(new ScatterSplitEnchantment());
    }

    @Override
    public void run(BootstrapContext<Enchantment> context) {
        for (BaseEnchantmentDefinition baseCustomEnchantment : customEnchantmentList){
            baseCustomEnchantment.bootstrap(context);
        }
    }
}
