package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedAndTridentMod;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.util.RandomInRegionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * 雷暴附魔
 */

public class ThunderFallEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final MapCodec<ThunderFallEnchantment> CODEC = MapCodec.unit(ThunderFallEnchantment::new);

    public static final ResourceKey<Enchantment> THUNDER_FALL = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedAndTridentMod.MODID , "thunder_fall"));

    public ThunderFallEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 5;
        minBaseCost = 1;
        minIncrementCost = 1;
        maxBaseCost = 2;
        maxIncrementCost = 3;
    }

    // todo ：随机的位置不能有重复
    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (entity.getPersistentData().getBoolean("ThunderFallTriggered")) return;
        entity.getPersistentData().putBoolean("ThunderFallTriggered", true);

        EntityType.LIGHTNING_BOLT.spawn(level, entity.getOnPos(), MobSpawnType.TRIGGERED);

        Set<BlockPos> locatePos = new HashSet<>();

        for (int incr = 0; incr < enchantmentLevel; incr++){
            for (int i = 0; i < 6; i++) {

                BlockPos randomPos = RandomInRegionUtil.getRandomPosInDiamond(entity.getOnPos(), 4, level);
                while (locatePos.contains(randomPos)){
                    randomPos = RandomInRegionUtil.getRandomPosInDiamond(entity.getOnPos(), 4, level);
                }


                EntityType.LIGHTNING_BOLT.spawn(level, randomPos, MobSpawnType.TRIGGERED);
            }
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, THUNDER_FALL, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(appliedOnItemType),
                weight,
                maxLevel,
                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                anvilCost,
                effectSoltPos)
        ).withEffect(EnchantmentEffectComponents.HIT_BLOCK, new ThunderFallEnchantment()));
    }
}
