package com.rzero.drownedandtrident.enchantment;

import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.enchantment.custom.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

/**
 * 附魔的datagen入口
 */
public class EnchantmentBootstrapper {

    private static List<BaseEnchantmentDefinition> customEnchantmentList = new ArrayList<>();

    static {
        customEnchantmentList.add(new FanShootEnchantment());
        customEnchantmentList.add(new ExplosiveShootEnchantment());
        customEnchantmentList.add(new ErosionEnchantment());
        customEnchantmentList.add(new ThunderStormEnchantment());
    }


    public static void bootstrap(BootstrapContext<Enchantment> context){
        for (BaseEnchantmentDefinition customEnchantment : customEnchantmentList){
            customEnchantment.bootstrap(context);
        }
    }

}
