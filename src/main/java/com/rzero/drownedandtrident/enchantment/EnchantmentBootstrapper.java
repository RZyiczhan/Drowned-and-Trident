package com.rzero.drownedandtrident.enchantment;

import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.enchantment.custom.ExplosiveShootEnchantment;
import com.rzero.drownedandtrident.enchantment.custom.FanShootEnchantment;
import com.rzero.drownedandtrident.enchantment.custom.ThunderStormEnchantment;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentBootstrapper {

    private static List<BaseEnchantmentDefinition> customEnchantmentList = new ArrayList<>();

    static {
        customEnchantmentList.add(new FanShootEnchantment());
        customEnchantmentList.add(new ThunderStormEnchantment());
        customEnchantmentList.add(new ExplosiveShootEnchantment());
    }


    public static void bootstrap(BootstrapContext<Enchantment> context){
        for (BaseEnchantmentDefinition customEnchantment : customEnchantmentList){
            customEnchantment.bootstrap(context);
        }
    }

}
