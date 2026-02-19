package com.rzero.drownedandtrident.enchantment;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 附魔注册
 */
public class DATEnchantmentFunctionRegister {

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> TRIDENT_ENCHANTMENT_EFFECT =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, DrownedandTrident.MODID);

    public static void register(IEventBus eventBus){
        new DATEnchantment();
        TRIDENT_ENCHANTMENT_EFFECT.register(eventBus);
     }


}
