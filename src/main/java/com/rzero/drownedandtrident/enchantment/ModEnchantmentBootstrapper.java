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
//        customEnchantmentList.add(new FanShootEnchantment());
        customEnchantmentList.add(new ExplosiveShootEnchantment());
        customEnchantmentList.add(new ShootAccelerationEnchantment());
        customEnchantmentList.add(new ThunderTrajectoryEnchantment());
    }

    @Override
    public void run(BootstrapContext<Enchantment> context) {
        for (BaseEnchantmentDefinition baseCustomEnchantment : customEnchantmentList){
            baseCustomEnchantment.bootstrap(context);
        }
    }
}
//
//Caused by:
//java.util.concurrent.CompletionException: java.lang.IllegalStateException:
//Couldn't generate file '/Users/zhanyichen/Desktop/个人兴趣/MC/MC mod & 整合包/MC mod开发/DrownedAndTrident（溺尸与三叉戟）/Drowned-and-Trident/src/generated/resources/data/drownedandtrident/enchantment/thunder_storm.json':
//Unregistered holder in ResourceKey[minecraft:root / minecraft:enchantment_entity_effect_type]:
//Direct{MapCodec[EmptyEncoder UnitDecoder[com.rzero.drownedandtrident.enchantment.custom.ThunderStormEnchantment@746fb276]]};
//Unregistered holder in ResourceKey[minecraft:root / minecraft:enchantment_entity_effect_type]:
//Direct{MapCodec[EmptyEncoder UnitDecoder[com.rzero.drownedandtrident.enchantment.custom.ThunderStormEnchantment@470163a4]]}

