package com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType;

import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;

public class  ModEnchantmentHelper {

    public static void onEntityTick(ServerLevel serverLevel, Entity entity, ItemStack entityCreatorItemSource, Vec3 currPos, LivingEntity entityCreator){
        runIterationOnItem(
                entityCreatorItemSource,
                new EnchantmentVisitor() {
                    @Override
                    public void accept(Holder<Enchantment> enchantment, int level) {
                        ModDefinedEnchantmentTriggerFunction.onEntityTick(serverLevel, entity, enchantment.value(), currPos, entityCreatorItemSource, entityCreator, level);
                    }
                }
        );
    }

    /**
     * 为三叉戟实体加速
     * @param serverLevel
     * @param entity 这必须是一个Projectile
     */
    public static void doAccelerateEffects(ServerLevel serverLevel, Entity entity, ItemStack weapon, Vec3 shootPos){
        if (entity instanceof DATThrownTrident) {
            runIterationOnItem(
                    weapon,
                    new EnchantmentVisitor() {
                        @Override
                        public void accept(Holder<Enchantment> enchantment, int level) {
                            ModDefinedEnchantmentTriggerFunction.doProjectileAccelerate(serverLevel, level, entity, enchantment.value(), shootPos) ;
                        }
                    }
            );
        }
    }

    /**
     * copy from EnchantmentHelper，作用，依次遍历物品上的每一个附魔，应用指定附魔触发器的触发方法
     * @param stack
     * @param visitor
     */
    public static void runIterationOnItem(ItemStack stack, ModEnchantmentHelper.EnchantmentVisitor visitor) {
        // 从itemStack上获取到了item上绑定的所有附魔
        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        // NeoForge 允许模组通过代码动态改变一个物品的附魔列表，而不需要真的把附魔写入物品的 NBT 数据里。（来自gemini 3 pro的解释）
        // Neo: Respect gameplay-only enchantments when doing iterations
        var lookup = net.neoforged.neoforge.common.CommonHooks.resolveLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
        if (lookup != null) {
            itemenchantments = stack.getAllEnchantments(lookup);
        }

        // 对获取到的物品上的每一个附魔，应用指定附魔触发器的触发方法
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            visitor.accept(entry.getKey(), entry.getIntValue());
        }
    }


    @FunctionalInterface
    public interface EnchantmentInSlotVisitor {
        void accept(Holder<Enchantment> enchantment, int level, EnchantedItemInUse item);
    }

    /**
     * copy from EnchantmentHelper
     */
    @FunctionalInterface
    public interface EnchantmentVisitor {
        void accept(Holder<Enchantment> enchantment, int level);
    }

}
