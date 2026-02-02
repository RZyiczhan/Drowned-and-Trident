package com.rzero.drownedandtrident.mixin.entity;

import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
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

}
