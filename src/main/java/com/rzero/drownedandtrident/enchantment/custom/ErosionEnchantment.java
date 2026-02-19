package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.modRegistry.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.enchantment.programmingConstant.DefaultEnchantmentUpgradeStatus;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;


/**
 * 侵蚀附魔
 */
public class ErosionEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final ResourceKey<Enchantment> EROSION = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "erosion"));

    public static final MapCodec<ErosionEnchantment> CODEC = MapCodec.unit(ErosionEnchantment::new);

    private static final List<Holder<MobEffect>> appliedEffectsList = List.of(
            MobEffects.DARKNESS,
            MobEffects.HUNGER,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.WEAKNESS
            );

    private static final List<Holder<MobEffect>> appliedEffectsListForUpgrade = List.of(
            MobEffects.POISON,
            MobEffects.CONFUSION,
            MobEffects.BLINDNESS,
            MobEffects.DARKNESS,
            MobEffects.HUNGER,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.WEAKNESS,
            MobEffects.WITHER
    );

    private static final Random random = new Random();

    public ErosionEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 5;
        minBaseCost = 1;
        minIncrementCost = 4;
        maxBaseCost = 12;
        maxIncrementCost = 5;
    }

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {

        var items = context.lookup(Registries.ITEM);

        register(context, EROSION, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(appliedOnItemType),
                weight,
                maxLevel,
                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                anvilCost,
                effectSoltPos)
        ).withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new ErosionEnchantment()));

    }

    /**
     * @param entity 在攻击方式是命名目标生效时，entity是被命中的目标，在攻击方式是命名方块生效时，entity是被附魔的东西本身
     */
    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (!(entity instanceof LivingEntity living)) return;

        ItemStack datTridentItem = item.itemStack();
        byte upgradeStatus = datTridentItem.getOrDefault(TridentDataComponentRegister.EROSION_UPGRADE_STATUS, DefaultEnchantmentUpgradeStatus.DEFAULT_EROSION_UPGRADE_STATUS);

        if (upgradeStatus == 0){
            // 药水效果无增幅为1级效果，所以实际增幅是附魔等级-1
            living.addEffect(new MobEffectInstance(
                    ErosionEnchantment.appliedEffectsList.get(ErosionEnchantment.random.nextInt(ErosionEnchantment.appliedEffectsList.size())),
                    100, enchantmentLevel-1));
        } else {
            Holder<MobEffect> firstEffect = ErosionEnchantment.appliedEffectsListForUpgrade.get(ErosionEnchantment.random.nextInt(ErosionEnchantment.appliedEffectsList.size()));
            Holder<MobEffect> secondEffect = firstEffect;
            while (secondEffect == firstEffect){
                secondEffect = ErosionEnchantment.appliedEffectsListForUpgrade.get(ErosionEnchantment.random.nextInt(ErosionEnchantment.appliedEffectsList.size()));
            }
            living.addEffect(new MobEffectInstance(firstEffect,160, enchantmentLevel-1));
            living.addEffect(new MobEffectInstance(secondEffect,160, enchantmentLevel-1));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
