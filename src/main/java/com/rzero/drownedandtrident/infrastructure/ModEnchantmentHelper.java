package com.rzero.drownedandtrident.infrastructure;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ModEnchantmentHelper {

    /**
     * POST_HIT 附魔触发器
     * @param level                 环境，客户端或服务端
     * @param hitTargetEntity       命中的目标生物实体
     * @param hitInitiator          命中行为发起方生物实体
     * @param projectileEntity      导致命中行为的抛物线载体实体
     * @param hitPos                命中位置
     * @param state
     * @param onBreak
     * @param weaponItem            生成“导致命中行为的抛物线载体实体”的ItemStack（就是物品）
     */
    public static void doPostHitEffectsWithItemSource(ServerLevel level,
                                                      @Nullable Entity hitTargetEntity,
                                                      @Nullable LivingEntity hitInitiator,
                                                      @Nullable Entity projectileEntity,
                                                      @Nullable Vec3 hitPos,
                                                      @Nullable BlockState state,
                                                      @Nullable Consumer<Item> onBreak,
                                                      @Nullable EquipmentSlot slot,
                                                      @Nullable DamageSource damageSource,
                                                      PostHitTypeEnum postHitType,
                                                      ItemStack weaponItem
                                                      ) {

        if (postHitType == PostHitTypeEnum.POST_ATTACK){
            EnchantmentHelper.doPostAttackEffectsWithItemSource(level, hitTargetEntity, damageSource, weaponItem);
        }
        else if (postHitType == PostHitTypeEnum.HIT_BLOCK) {
            EnchantmentHelper.onHitBlock(level, weaponItem, hitInitiator, projectileEntity, slot, hitPos, state, onBreak);
        }

    }


    public static void doPostAttackEffectsWithItemSource(ServerLevel level, Entity entity, DamageSource damageSource, @Nullable ItemStack itemSource) {
        if (entity instanceof LivingEntity livingentity) {
            EnchantmentHelper.runIterationOnEquipment(
                    livingentity,
                    (p_344427_, p_344428_, p_344429_) -> p_344427_.value()
                            .doPostAttack(level, p_344428_, p_344429_, EnchantmentTarget.VICTIM, entity, damageSource)
            );
        }

        if (itemSource != null && damageSource.getEntity() instanceof LivingEntity livingentity1) {
            EnchantmentHelper.runIterationOnItem(
                    itemSource,
                    EquipmentSlot.MAINHAND,
                    livingentity1,
                    (p_344557_, p_344558_, p_344559_) -> p_344557_.value()
                            .doPostAttack(level, p_344558_, p_344559_, EnchantmentTarget.ATTACKER, entity, damageSource)
            );
        }
    }


//    public void doPostAttack(
//            ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, EnchantmentTarget target, Entity entity, DamageSource damageSource
//    ) {
//        for (TargetedConditionalEffect<EnchantmentEntityEffect> targetedconditionaleffect : this.getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
//            if (target == targetedconditionaleffect.enchanted()) {
//                doPostAttack(targetedconditionaleffect, level, enchantmentLevel, item, entity, damageSource);
//            }
//        }
//    }
//


//    public void onHitBlock(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 pos, BlockState state) {
//        applyEffects(
//                this.getEffects(EnchantmentEffectComponents.HIT_BLOCK),
//                blockHitContext(level, enchantmentLevel, entity, pos, state),
//                p_346325_ -> p_346325_.apply(level, enchantmentLevel, item, entity, pos)
//        );
//    }


    public static void onHitBlock(
            ServerLevel level,
            ItemStack stack,
            @Nullable LivingEntity owner,
            Entity entity,
            @Nullable EquipmentSlot slot,
            Vec3 pos,
            BlockState state,
            Consumer<Item> onBreak
    ) {
        EnchantedItemInUse enchantediteminuse = new EnchantedItemInUse(stack, slot, owner, onBreak);
        EnchantmentHelper.runIterationOnItem(
                stack, (p_350196_, p_350197_) -> p_350196_.value().onHitBlock(level, p_350197_, enchantediteminuse, entity, pos, state)
        );
    }

}
