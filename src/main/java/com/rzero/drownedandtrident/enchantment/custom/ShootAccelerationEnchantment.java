package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 三叉戟抛物线实体生成时，额外增加速度的速度附魔
 */
public class ShootAccelerationEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final ResourceKey<Enchantment> SHOOT_ACCELERATION = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "shoot_acceleration"));

    public static final MapCodec<ShootAccelerationEnchantment> CODEC = MapCodec.unit(ShootAccelerationEnchantment::new);
    private static final Logger log = LoggerFactory.getLogger(ShootAccelerationEnchantment.class);

    /**
     * 最高2级
     */
    public ShootAccelerationEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 1;
        minBaseCost = 20;
        minIncrementCost = 1;
        maxBaseCost = 25;
        maxIncrementCost = 3;
    }


    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, SHOOT_ACCELERATION, Enchantment.enchantment(Enchantment.definition(
                                items.getOrThrow(appliedOnItemType),
                                weight,
                                maxLevel,
                                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                                anvilCost,
                                effectSoltPos)
                        )
                // 注意：withEffect绑定的参数是effect类型
                        .withEffect(TridentEnchantmentTriggerTypeRegister.ON_ENTITY_CREATE.get(), new ShootAccelerationEnchantment())
        );
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (!(entity instanceof DATThrownTrident datThrownTrident)){
            return;
        }

        // 这个是客户端渲染契约上能接受的最大速度了，在最大就得想办法修改客户端的渲染契约和渲染引擎了
        datThrownTrident.setVelocity(3.9F);

    }

    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

}
