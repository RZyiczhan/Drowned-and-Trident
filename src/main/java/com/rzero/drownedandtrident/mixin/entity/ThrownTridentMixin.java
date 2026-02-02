package com.rzero.drownedandtrident.mixin.entity;

import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin {

    private static final Logger log = LoggerFactory.getLogger(ThrownTridentMixin.class);

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

        ModEnchantmentHelper.onEntityTick(
                serverlevel,
                thrownTrident,
                thrownTrident.getWeaponItem(),
                new Vec3(thrownTrident.getX(), thrownTrident.getY(), thrownTrident.getZ()),
                thrownTrident.getOwner() instanceof LivingEntity owner ? owner : null
        );
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

}
