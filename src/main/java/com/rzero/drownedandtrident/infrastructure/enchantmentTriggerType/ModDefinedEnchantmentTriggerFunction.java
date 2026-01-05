package com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class ModDefinedEnchantmentTriggerFunction {

//    public static void doPostAttack(
//            TargetedConditionalEffect<EnchantmentEntityEffect> effect,
//            ServerLevel level,
//            int enchantmentLevel,
//            EnchantedItemInUse item,
//            Entity p_entity,
//            DamageSource damageSource
//    ) {
//        if (effect.matches(damageContext(level, enchantmentLevel, p_entity, damageSource))) {
//            Entity entity = switch (effect.affected()) {
//                case ATTACKER -> damageSource.getEntity();
//                case DAMAGING_ENTITY -> damageSource.getDirectEntity();
//                case VICTIM -> p_entity;
//            };
//            if (entity != null) {
//                effect.effect().apply(level, enchantmentLevel, item, entity, entity.position());
//            }
//        }
//    }
//
//    public static void doPostAttack(
//            ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, EnchantmentTarget target, Entity entity, DamageSource damageSource, Enchantment enchantment
//    ) {
//        // todo : 这里为什么遍历了两层，如果这里是遍历所有post——attack触发方式的附魔，那上一层runIteration在遍历什么东西
//        for (TargetedConditionalEffect<EnchantmentEntityEffect> targetedconditionaleffect : enchantment.getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
//            if (target == targetedconditionaleffect.enchanted()) {
//                doPostAttack(targetedconditionaleffect, level, enchantmentLevel, item, entity, damageSource);
//            }
//        }
//    }


    public static void onEntityTick(ServerLevel level, Entity projectile, Enchantment enchantment, Vec3 entityCurrentPos, ItemStack entityCreatorItemSource, LivingEntity entityCreator, int enchantmentLevel){
        for (ConditionalEffect<EnchantmentEntityEffect> conditionalEffect : enchantment.getEffects(TridentEnchantmentTriggerTypeRegister.ON_ENTITY_TICK.get())){
            applySingleOnEntityTickEnchantment(conditionalEffect, level, enchantmentLevel, projectile, entityCurrentPos, new EnchantedItemInUse(entityCreatorItemSource, EquipmentSlot.MAINHAND, entityCreator));
        }
    }

    public static void applySingleOnEntityTickEnchantment(ConditionalEffect<EnchantmentEntityEffect> conditionalEnchantmentEffect,
                                                          ServerLevel level,
                                                          int enchantmentLevel,
                                                          Entity projectile,
                                                          Vec3 entityCurrentPos,
                                                          EnchantedItemInUse enchantedEntityCreatorItemSource
    ) {
        conditionalEnchantmentEffect.effect().apply(
                level,
                enchantmentLevel,
                enchantedEntityCreatorItemSource,
                projectile,
                entityCurrentPos
        );
    }

    /**
     * 获取物品上每一个“创建实体时”触发器的附魔并依次apply
     * @param level
     * @param enchantmentLevel
     * @param projectile
     * @param enchantment
     */
    public static void doProjectileAccelerate(ServerLevel level, int enchantmentLevel, Entity projectile, Enchantment enchantment, Vec3 createPos){
        for (ConditionalEffect<EnchantmentEntityEffect> conditionalEffect : enchantment.getEffects(TridentEnchantmentTriggerTypeRegister.ON_ENTITY_CREATE.get())){
            applySingleOnEntityCreateEnchantment(conditionalEffect, level, enchantmentLevel, projectile, createPos);
        }
    }


    /**
     * 应用这个绑定到“创建实体时”触发器的附魔
     * @param conditionalEnchantmentEffect
     * @param level
     * @param enchantmentLevel
     * @param projectile
     */
    private static void applySingleOnEntityCreateEnchantment(
            ConditionalEffect<EnchantmentEntityEffect> conditionalEnchantmentEffect,
            ServerLevel level,
            int enchantmentLevel,
            Entity projectile,
            Vec3 createPos
    ) {

        // 这里本质上是利用lootContextParamSet构建LootContext
        // 对比筛掉不需要的context情况不触发附魔
//        if (!conditionalEnchantmentEffect.matches(new LootContext())){
//
//        }
        conditionalEnchantmentEffect.effect().apply(
                level, enchantmentLevel, null, projectile, createPos);
    }

}
