package com.rzero.drownedandtrident.mixin.mixinImpl.entity;

import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import com.rzero.drownedandtrident.mixin.mixinInterface.IThrownTridentExt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
// 这里的 implements 告诉 Mixin 处理器：
// "在融合的时候，把 'implements IThrownTridentExt' 这行字也写到 ThrownTrident 类的头上去！"
public class ThrownTridentMixin implements IThrownTridentExt {

    private static final Logger log = LoggerFactory.getLogger(ThrownTridentMixin.class);

    /**
     * 命名成这种格式的原因是：
     * 1）防止MC未来更新时在ThrownTrident出现一个名为hadBeenHit的字段
     * 2）防止其他mod也在ThrownTrident新增一个名为hadBeenHit的字段，
     *      这样除非对方模组的modid和这个模组的一样，否则就不会出现字段名撞车的情况
     */
    @Unique
    private boolean drownedandtrident$hadBeenHit = false;

    /**
     * 1）射出后经过1个Tick后落第一发雷，正式开始循环周期（早点落下第一道雷和第二道雷（3Tick内两道），给用户附魔已生效的快速反馈）
     * 2）平均2.5Tick落一道雷，由于第2.5Tick这种概念技术上不存在，所以是 2Tick后劈第一次，
     * 然后3Tick后劈第二次视为一个标准循环周期，这样平均下来就是5Tick里劈了两次雷，平均2.5Tick一次
     */
    @Unique
    private short drownedandtrident$thunderTrajectoryTriggerCountTick = 4;

    /**
     * 每个Tick结算时，触发关联onEntityTick触发时机的附魔
     */
    @Inject(method = "tick", at = @At("TAIL"), remap = true)
    private void onTickEnd(CallbackInfo ci) {

        // 原理：先转成 Object 骗过编译器，再转回目标类
        ThrownTrident thrownTrident = (ThrownTrident) (Object) this;

        // 检查是不是客户端，通常逻辑只在服务端跑
        if (!(thrownTrident.level() instanceof ServerLevel serverlevel)) {
            return;
        }

        if (drownedandtrident$thunderTrajectoryTriggerCountTick < 5){
            drownedandtrident$thunderTrajectoryTriggerCountTick++;
        } else {
            drownedandtrident$thunderTrajectoryTriggerCountTick = 0;
        }

        if (!drownedandtrident$hadBeenHit) {
            ModEnchantmentHelper.onEntityTick(
                    serverlevel,
                    thrownTrident,
                    thrownTrident.getWeaponItem(),
                    new Vec3(thrownTrident.getX(), thrownTrident.getY(), thrownTrident.getZ()),
                    thrownTrident.getOwner() instanceof LivingEntity owner ? owner : null
            );
        }


    }


    /**
     * 在命中实体时，同时触发命中方块的调用
     */
    @Inject(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;" +
                            "doPostAttackEffectsWithItemSource(" +
                            "Lnet/minecraft/server/level/ServerLevel;" +
                            "Lnet/minecraft/world/entity/Entity;" +
                            "Lnet/minecraft/world/damagesource/DamageSource;" +
                            "Lnet/minecraft/world/item/ItemStack;" +
                            ")V",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeOnHitEntityPostAttackEnchantmentTriggered(EntityHitResult result, CallbackInfo ci) {

        // 原理：先转成 Object 骗过编译器，再转回目标类
        ThrownTrident thrownTrident = (ThrownTrident) (Object) this;

        // 检查是不是客户端，通常逻辑只在服务端跑
        if (!(thrownTrident.level() instanceof ServerLevel serverlevel)) {
            return;
        }

        EnchantmentHelper.onHitBlock(
                serverlevel,
                thrownTrident.getWeaponItem(),
                thrownTrident.getOwner() instanceof LivingEntity livingentity ? livingentity : null,
                thrownTrident,
                null,
                thrownTrident.getEyePosition(),
                serverlevel.getBlockState(result.getEntity().getOnPos()),
                item -> thrownTrident.kill());
    }

    @Inject(method = "hitBlockEnchantmentEffects", at = @At("TAIL"))
    private void onHitBlockEnchantmentEffectsEnd(CallbackInfo ci){
        this.drownedandtrident$setHadBeenHit(true);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void onOnHitEntityEnd(CallbackInfo ci){
        this.drownedandtrident$setHadBeenHit(true);
    }


    @Override
    public boolean drownedandtrident$isHadBeenHit() {
        return drownedandtrident$hadBeenHit;
    }

    @Override
    public void drownedandtrident$setHadBeenHit(boolean value) {
        this.drownedandtrident$hadBeenHit = value;
    }

    @Override
    public short drownedandtrident$getThunderTrajectoryTriggerCountTick() {
        return drownedandtrident$thunderTrajectoryTriggerCountTick;
    }

    @Override
    public void drownedandtrident$setThunderTrajectoryTriggerCountTick(short value) {
        drownedandtrident$thunderTrajectoryTriggerCountTick = value;
    }
}
