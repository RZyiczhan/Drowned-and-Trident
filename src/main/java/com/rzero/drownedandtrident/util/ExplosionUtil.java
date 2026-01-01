package com.rzero.drownedandtrident.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ExplosionUtil {

    // 基础伤害值 (可根据需要调整)
    private static final float BASE_DAMAGE = 20.0F;

    /**
     * 手动模拟爆炸
     * 1. 如果在水中：不破坏方块，伤害减半，生成气泡粒子。
     * 2. 如果不在水中：破坏方块，30%概率掉落物品，全额伤害。
     *
     * @param level  世界
     * @param center 爆炸中心
     * @param radius 爆炸半径
     * @param source 爆炸来源实体 (可为 null，用于伤害归属)
     */
    public static void explode(Level level, Vec3 center, float radius, Entity source) {
        if (level.isClientSide) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos centerPos = BlockPos.containing(center);

        // --- 1. 环境检测 (是否在水下) ---
        FluidState fluidState = level.getFluidState(centerPos);
        boolean isUnderwater = fluidState.is(FluidTags.WATER);

        // --- 2. 视觉与听觉效果 ---
        if (isUnderwater) {
            // 水下音效
            level.playSound(null, centerPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
            // 水下粒子 (大量气泡)
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, center.x, center.y, center.z, 50, radius / 2, radius / 2, radius / 2, 0.1);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y, center.z, 5, 1, 1, 1, 0); // 小型爆炸
        } else {
            // 陆地音效
            level.playSound(null, centerPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
            // 陆地粒子 (巨大爆炸)
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 1, 0, 0, 0, 0);
        }

        // --- 3. 方块破坏逻辑 (仅当不在水下时执行) ---
        if (!isUnderwater) {
            handleBlockDestruction(serverLevel, center, centerPos, radius);
        }

        // --- 4. 实体伤害逻辑 (在水下伤害降低) ---
        float damageAmount = isUnderwater ? BASE_DAMAGE * 0.4F : BASE_DAMAGE; // 水下伤害降低为 40%
        handleEntityDamage(serverLevel, center, radius, source, damageAmount);
    }

    private static void handleBlockDestruction(ServerLevel level, Vec3 center, BlockPos centerPos, float radius) {
        int r = (int) Math.ceil(radius);
        List<BlockPos> blocksToBreak = new ArrayList<>();

        // 收集范围内的方块
        for (int x = centerPos.getX() - r; x <= centerPos.getX() + r; x++) {
            for (int y = centerPos.getY() - r; y <= centerPos.getY() + r; y++) {
                for (int z = centerPos.getZ() - r; z <= centerPos.getZ() + r; z++) {
                    BlockPos targetPos = new BlockPos(x, y, z);

                    // 球体距离判定
                    if (targetPos.getCenter().distanceToSqr(center) <= radius * radius) {
                        BlockState state = level.getBlockState(targetPos);
                        // 跳过空气、基岩、液体
                        if (!state.isAir() && state.getDestroySpeed(level, targetPos) != -1.0F && state.getFluidState().isEmpty()) {
                            blocksToBreak.add(targetPos);
                        }
                    }
                }
            }
        }

        // 执行破坏
        for (BlockPos pos : blocksToBreak) {
            // 核心逻辑：30% 概率掉落
            // nextFloat() 返回 0.0 到 1.0 之间的数。小于 0.3 即为 30% 概率。
            boolean dropItem = level.random.nextFloat() < 0.3F;

            // destroyBlock 第二个参数控制是否掉落
            level.destroyBlock(pos, dropItem);

            // 生成少量碎片粒子
            if (level.random.nextInt(10) == 0) {
                level.sendParticles(ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }

    private static void handleEntityDamage(ServerLevel level, Vec3 center, float radius, Entity source, float damageAmount) {
        // 获取爆炸范围内的所有实体
        AABB damageArea = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );

        List<Entity> entities = level.getEntities(source, damageArea); // 排除发射者自己(如果需要排除的话，不排除传null)

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                double distSq = entity.position().distanceToSqr(center);

                // 确保实体在球体半径内
                if (distSq <= radius * radius) {
                    // 距离衰减：离中心越近伤害越高 (可选)
                    double distanceFactor = 1.0 - (Math.sqrt(distSq) / radius);
                    float finalDamage = (float) (damageAmount * distanceFactor);
                    if (finalDamage < 1.0f) finalDamage = 1.0f;

                    // 造成爆炸伤害
                    // 使用 level.damageSources().explosion 可以在死亡信息中正确显示“被炸死”
                    entity.hurt(level.damageSources().explosion(source, null), finalDamage);

                    // 可选：施加爆炸击退 (简单模拟)
                    Vec3 knockback = entity.position().subtract(center).normalize().scale(0.5 + distanceFactor);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
                }
            }
        }
    }
}