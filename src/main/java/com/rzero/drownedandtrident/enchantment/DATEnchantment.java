package com.rzero.drownedandtrident.enchantment;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.enchantment.custom.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

import java.util.function.Supplier;

public class DATEnchantment {

    // 这行本质上是把thunder_storm这个string和其CODEC标识符绑定起来
    // 方便创建ResourceKey
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> FAN_SPLIT =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("fan_split", () -> FanSplitEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> SCATTER_SPLIT =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("scatter_split", () -> ScatterSplitEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> EXPLOSIVE_SHOOT =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("explosive_shoot", () -> ExplosiveShootEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> EROSION =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("erosion", () -> ErosionEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> THUNDER_STORM =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("thunder_storm", () -> ThunderStormEnchantment.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> THUNDER_TRAJECTORY =
            DATEnchantmentFunctionRegister.TRIDENT_ENCHANTMENT_EFFECT.register("thunder_tarjectory", () -> ThunderTrajectoryEnchantment.CODEC);

}
