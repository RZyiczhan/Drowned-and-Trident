package com.rzero.drownedandtrident.util;

import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import com.rzero.drownedandtrident.programmingConstant.DefaultTridentSplitParamConstant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ProjectileSplitUtil {

    // todo：貌似生成的三叉戟的投掷朝向比原三叉戟高一点
    public static void generateFanSplitPairTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick, ThrownTrident thrownTrident, ItemStack stackWithoutNonMigratingEnchantment, double degree){
        float angleRadians = (float) Math.toRadians(degree);
        generateSingleFanSplitTrident(level, tridentOwner, delayedTick, thrownTrident, stackWithoutNonMigratingEnchantment, angleRadians);
        generateSingleFanSplitTrident(level, tridentOwner, delayedTick, thrownTrident, stackWithoutNonMigratingEnchantment, -angleRadians);
    }

    private static void generateSingleFanSplitTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick, ThrownTrident thrownTrident, ItemStack stackWithoutNonMigratingEnchantment, float angleRadians){

        Vec3 currentVectorWithVelocity = thrownTrident.getDeltaMovement();
        Vec3 splitPos = thrownTrident.getEyePosition();
        // 修改投射角度
        Vec3 copiedTridentVelocity = currentVectorWithVelocity.yRot(angleRadians);

        generateSingleTrident(level, tridentOwner, delayedTick, stackWithoutNonMigratingEnchantment, copiedTridentVelocity, splitPos);
    }


    public static void generateScatterSplitTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick,
                                                   ThrownTrident thrownTrident, ItemStack stackWithoutNonMigratingEnchantment,
                                                   int spreadLevel, int generateCnt){

        Vec3 currentVectorWithVelocity = thrownTrident.getDeltaMovement();
        Vec3 splitPos = thrownTrident.getEyePosition();

        Random random = new Random();

        float spread = spreadLevel / 10f;

        for (int serialNumber = 0; serialNumber < generateCnt; serialNumber++) {

            double offsetX = random.nextGaussian() * spread;
            double offsetY = random.nextGaussian() * spread;
            double offsetZ = random.nextGaussian() * spread;

            Vec3 copiedTridentVelocity = currentVectorWithVelocity.add(offsetX, offsetY, offsetZ);
            generateSingleTrident(level, tridentOwner, delayedTick, stackWithoutNonMigratingEnchantment, copiedTridentVelocity, splitPos);
        }

    }

    private static void generateSingleTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick,
                                              ItemStack stackWithoutNonMigratingEnchantment,
                                              Vec3 copiedTridentVelocity, Vec3 splitPos){

        // 修改分裂出的三叉戟的分裂Tick延时
        int originalFanSplitTick = stackWithoutNonMigratingEnchantment.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK);
        originalFanSplitTick -= delayedTick;
        int originalScatterSplitTick = stackWithoutNonMigratingEnchantment.getOrDefault(TridentDataComponentRegister.SCATTER_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK);
        originalScatterSplitTick -= delayedTick;

        // 虽然理论上克隆出来的三叉戟已经不再具有已执行的分裂附魔了
        // 但出于以防万一的考虑，将延后游戏刻数归0的附魔延后Tick额外-1，以避免走入Tick Scheduler中：“延后Tick为0 = 立刻触发”的逻辑
        if (originalFanSplitTick == 0) originalFanSplitTick--;
        if (originalScatterSplitTick == 0) originalScatterSplitTick--;

        // 注意这里不能把dataComponent设置回stackWithoutNonMigratingEnchantment
        // 这个itemStack还要被其他复制出来的三叉戟复用呢，
        // set回去只会导致同批出来还在等待执行分裂出来的三叉戟们拿到初始分裂Tick是错误的（是已经被上一个分裂出来的三叉戟修改过的版本）
        // 错误警示：
        // stackWithoutNonMigratingEnchantment.set(TridentDataComponentRegister.FAN_SPLIT_TICK, originalFanSplitTick);
        // stackWithoutNonMigratingEnchantment.set(TridentDataComponentRegister.SCATTER_SPLIT_TICK, originalScatterSplitTick);
        // 这里应该copy一个新的itemStack给生成出来的新三叉戟

        ItemStack stackForCloneTrident = stackWithoutNonMigratingEnchantment.copy();
        stackForCloneTrident.set(TridentDataComponentRegister.FAN_SPLIT_TICK, originalFanSplitTick);
        stackForCloneTrident.set(TridentDataComponentRegister.SCATTER_SPLIT_TICK, originalScatterSplitTick);

        // 设置克隆出来的三叉戟的初始速度和位置
        ThrownTrident cloneThrownTrident = new ThrownTrident(level, tridentOwner, stackForCloneTrident);
        cloneThrownTrident.setDeltaMovement(copiedTridentVelocity);

        cloneThrownTrident.setPos(splitPos);
        cloneThrownTrident.pickup = AbstractArrow.Pickup.DISALLOWED;

        // 强制同步一下实体的朝向，让模型看起来也是歪着飞的
        // 虽然 Projectile.tick() 会自动更新，但手动设置一下更丝滑
        double d0 = copiedTridentVelocity.horizontalDistance();
        cloneThrownTrident.setYRot((float)(Math.atan2(copiedTridentVelocity.x, copiedTridentVelocity.z) * (double)(180F / (float)Math.PI)));
        cloneThrownTrident.setXRot((float)(Math.atan2(copiedTridentVelocity.y, d0) * (double)(180F / (float)Math.PI)));
        cloneThrownTrident.yRotO = cloneThrownTrident.getYRot();
        cloneThrownTrident.xRotO = cloneThrownTrident.getXRot();

        // 因为新的三叉戟是通过add的方式进的世界，所以没有shootFromDirection，没有调里面的OnEntityInit和AfterEntityInit
        // 手动触发一下AfterEntityInit绑定的附魔
        ModEnchantmentHelper.doAfterEntityInit(level, cloneThrownTrident, stackWithoutNonMigratingEnchantment, splitPos, tridentOwner);

        // 加入世界，开始由物理引擎接管
        level.addFreshEntity(cloneThrownTrident);
    }

}
