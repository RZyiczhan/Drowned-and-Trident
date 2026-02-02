package com.rzero.drownedandtrident.mixin.entity;

import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    private static final Logger log = LoggerFactory.getLogger(ProjectileMixin.class);

    /**
     * 发射三叉戟后，触发关联了AfterEntityInit触发时机的附魔
     * */
    @Inject(method = "shootFromRotation", at = @At("TAIL"))
    private void onShootFromRotationForThrownTrident(CallbackInfo ci){

        log.info("projectile enter");

        Projectile projectile = (Projectile) (Object) this;
        // 只作用于三叉戟
        if (!(projectile instanceof ThrownTrident thrownTrident)) return;

        // 检查是不是客户端，通常逻辑只在服务端跑
        if (!(thrownTrident.level() instanceof ServerLevel serverlevel)) {
            return;
        }

        ModEnchantmentHelper.doAfterEntityInit(serverlevel, thrownTrident, thrownTrident.getWeaponItem(), thrownTrident.getEyePosition(),
                thrownTrident.getOwner() instanceof LivingEntity owner ? owner : null);

    }


}
