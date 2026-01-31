package com.rzero.drownedandtrident.entity.addition;

import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * 一些覆盖原有溺尸自带方法的实现，用于提供给模组来外部调用
 */
public class DrownedAddition {

    /**
     * 覆盖溺尸进行远程攻击的手段，这样来让溺尸可以抛射自定义的三叉戟
     */
    public static void performRangedAttack(Drowned drowned, LivingEntity target, ItemStack drownedHoldItemStack) {
        DATThrownTrident datThrownTrident = new DATThrownTrident(drowned.level(), drowned, drownedHoldItemStack);
        double d0 = target.getX() - drowned.getX();
        double d1 = target.getY(0.3333333333333333) - datThrownTrident.getY();
        double d2 = target.getZ() - drowned.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        datThrownTrident.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - drowned.level().getDifficulty().getId() * 4));
        // 因为不想重写shoot，所以简单的在这里调用一下AfterEntityInit的附魔
        if (drowned.level() instanceof ServerLevel serverlevel) {
            ModEnchantmentHelper.doAfterEntityInit(serverlevel, datThrownTrident, drownedHoldItemStack,
                    new Vec3(datThrownTrident.getX(), datThrownTrident.getY(), datThrownTrident.getZ()), drowned);
        }
        drowned.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (drowned.getRandom().nextFloat() * 0.4F + 0.8F));
        drowned.level().addFreshEntity(datThrownTrident);
    }

}
