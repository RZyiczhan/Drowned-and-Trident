package com.rzero.drownedandtrident.util;

import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import com.rzero.drownedandtrident.programmingModel.EnchantmentsUpgradeSummary;
import com.rzero.drownedandtrident.programmingModel.TridentSplitParamModel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ProjectileSplitUtil {

    // todo：貌似生成的三叉戟的投掷朝向比原三叉戟高一点
    public static void generateFanSplitPairTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick, DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment, double degree){
        float angleRadians = (float) Math.toRadians(degree);
        generateSingleFanSplitTrident(level, tridentOwner, delayedTick, originDATThrownTrident, stackWithoutNonMigratingEnchantment, angleRadians);
        generateSingleFanSplitTrident(level, tridentOwner, delayedTick, originDATThrownTrident, stackWithoutNonMigratingEnchantment, -angleRadians);
    }

    private static void generateSingleFanSplitTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick, DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment, float angleRadians){

        Vec3 currentVectorWithVelocity = originDATThrownTrident.getDeltaMovement();
        Vec3 splitPos = originDATThrownTrident.getEyePosition();
        // 修改投射角度
        Vec3 copiedTridentVelocity = currentVectorWithVelocity.yRot(angleRadians);

        generateSingleTrident(level, tridentOwner, delayedTick, stackWithoutNonMigratingEnchantment, copiedTridentVelocity, splitPos, originDATThrownTrident.getSplitParam(), originDATThrownTrident.getEnchantmentsUpgradeSummary());
    }


    public static void generateScatterSplitTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick,
                                                   DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment,
                                                   int spreadLevel, int generateCnt){

        Vec3 currentVectorWithVelocity = originDATThrownTrident.getDeltaMovement();
        Vec3 splitPos = originDATThrownTrident.getEyePosition();

        Random random = new Random();

        float spread = spreadLevel / 10f;

        for (int serialNumber = 0; serialNumber < generateCnt; serialNumber++) {

            double offsetX = random.nextGaussian() * spread;
            double offsetY = random.nextGaussian() * spread;
            double offsetZ = random.nextGaussian() * spread;

            Vec3 copiedTridentVelocity = currentVectorWithVelocity.add(offsetX, offsetY, offsetZ);
            generateSingleTrident(level, tridentOwner, delayedTick, stackWithoutNonMigratingEnchantment, copiedTridentVelocity, splitPos, originDATThrownTrident.getSplitParam(), originDATThrownTrident.getEnchantmentsUpgradeSummary());
        }

    }

    private static void generateSingleTrident(ServerLevel level, LivingEntity tridentOwner, int delayedTick,
                                              ItemStack stackWithoutNonMigratingEnchantment,
                                              Vec3 copiedTridentVelocity, Vec3 splitPos, TridentSplitParamModel splitParam,
                                              EnchantmentsUpgradeSummary enchantmentsUpgradeSummary){

        // 修改分裂出的三叉戟的分裂Tick延时
        TridentSplitParamModel newSplitParam =  new TridentSplitParamModel(splitParam, delayedTick);

        DATThrownTrident cloneThrownTrident = new DATThrownTrident(level, tridentOwner, stackWithoutNonMigratingEnchantment, newSplitParam, enchantmentsUpgradeSummary);
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
