package com.rzero.drownedandtrident.util;

import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ProjectileSplitUtil {

    // todo：貌似生成的三叉戟的投掷朝向比原三叉戟高一点
    public static void generateSplitPairTrident(ServerLevel level, LivingEntity tridentOwner, DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment, double degree){
        float angleRadians = (float) Math.toRadians(degree);
        generateSingleTrident(level, tridentOwner, originDATThrownTrident, stackWithoutNonMigratingEnchantment, angleRadians);
        generateSingleTrident(level, tridentOwner, originDATThrownTrident, stackWithoutNonMigratingEnchantment, -angleRadians);
    }

    private static void generateSingleTrident(ServerLevel level, LivingEntity tridentOwner, DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment, float angleRadians){

        Vec3 currentVectorWithVelocity = originDATThrownTrident.getDeltaMovement();
        Vec3 splitPos = originDATThrownTrident.getEyePosition();
        Vec3 copiedTridentVelocity = currentVectorWithVelocity.yRot(angleRadians);

        DATThrownTrident cloneThrownTrident = new DATThrownTrident(level, tridentOwner, stackWithoutNonMigratingEnchantment);
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

        // 加入世界
        level.addFreshEntity(cloneThrownTrident);
    }

}
