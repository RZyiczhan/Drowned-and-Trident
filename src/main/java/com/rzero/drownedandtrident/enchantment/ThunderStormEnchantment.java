package com.rzero.drownedandtrident.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ThunderStormEnchantment() implements EnchantmentEntityEffect {

    public static final MapCodec<ThunderStormEnchantment> CODEC = MapCodec.unit(ThunderStormEnchantment::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        // todo：如何分波次触发
        // todo：如何保证三叉戟更换位置不二次触发（例子：初次所命中方块被烧毁后，三叉戟再次下落时不能二次触发）

        EntityType.LIGHTNING_BOLT.spawn(level, entity.getOnPos(), MobSpawnType.TRIGGERED);

        int wave = enchantmentLevel;
        int intervalTicks = 10;

        int delay = wave * intervalTicks;

        for (int i = 0; i < 3; i++) {
            BlockPos randomPos = getRandomPosInDiamond(entity.getOnPos(), 5, level);
            EntityType.LIGHTNING_BOLT.spawn(level, randomPos, MobSpawnType.TRIGGERED);
        }

    }


    // 工具：随机菱形范围内位置（曼哈顿距离 <= radius）
    private BlockPos getRandomPosInDiamond(BlockPos center, int radius, ServerLevel level) {
        RandomSource random = level.getRandom();
        BlockPos pos;

        while (true) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;

            if (Math.abs(dx) + Math.abs(dz) <= radius) {
                // 找到符合曼哈顿距离的位置
                pos = center.offset(dx, 0, dz);
                break;
            }
        }

        return pos;
    }


    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
