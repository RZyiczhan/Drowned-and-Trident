package com.rzero.drownedandtrident.enchantment;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.custom.ErosionEnchantment;
import com.rzero.drownedandtrident.enchantment.custom.ExplosiveShootEnchantment;
import com.rzero.drownedandtrident.enchantment.custom.FanShootEnchantment;
import com.rzero.drownedandtrident.enchantment.custom.ThunderStormEnchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 附魔注册
 */
public class TridentEnchantmentRegister{

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECT =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, DrownedandTrident.MODID);

    // 这行本质上是把thunder_storm这个string和其CODEC标识符绑定起来
    // 方便创建ResourceKey
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> FAN_SHOOT =
            ENTITY_ENCHANTMENT_EFFECT.register("fan_shoot", () -> FanShootEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> EXPLOSIVE_SHOOT =
            ENTITY_ENCHANTMENT_EFFECT.register("explosive_shoot", () -> ExplosiveShootEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> EROSION =
            ENTITY_ENCHANTMENT_EFFECT.register("erosion", () -> ErosionEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> THUNDER_STORM =
            ENTITY_ENCHANTMENT_EFFECT.register("thunder_storm", () -> ThunderStormEnchantment.CODEC);

    public static void register(IEventBus eventBus){
        ENTITY_ENCHANTMENT_EFFECT.register(eventBus);
     }


}
