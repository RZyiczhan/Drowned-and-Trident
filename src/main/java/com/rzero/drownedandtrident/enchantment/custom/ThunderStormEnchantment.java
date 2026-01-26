package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.AttackerProtectLightning.AttackerProtectLightning;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.event.tickSchedular.TickScheduler;
import com.rzero.drownedandtrident.util.PositionUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 命中后，阵雷批次在命中位置附近下落的附魔
 */
public class ThunderStormEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final MapCodec<ThunderStormEnchantment> CODEC = MapCodec.unit(ThunderStormEnchantment::new);

    public static final ResourceKey<Enchantment> THUNDER_STORM = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "thunder_storm"));


    public ThunderStormEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 5;
        minBaseCost = 6;
        minIncrementCost = 4;
        maxBaseCost = 12;
        maxIncrementCost = 6;
    }

    // todo: 如何让三叉戟命中生物也生效，且之后不再生效

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, THUNDER_STORM, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(appliedOnItemType),
                weight,
                maxLevel,
                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                anvilCost,
                effectSoltPos)
                )
                .withEffect(EnchantmentEffectComponents.HIT_BLOCK, new ThunderStormEnchantment())
        );
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (entity.getPersistentData().getBoolean("ThunderStormTriggered")) return;
        entity.getPersistentData().putBoolean("ThunderStormTriggered", true);

        if (Objects.isNull(item.owner())){
            return;
        }

        if (!(entity instanceof DATThrownTrident datThrownTrident)) return;

        byte upgradeStatus = datThrownTrident.getEnchantmentsUpgradeSummary().getThunderStormUpgradeStatus();

        AttackerProtectLightning.spawnAttackProtectLightning(level, entity.getOnPos(), item.owner());

        BlockPos targetPos = new BlockPos(entity.getOnPos().getX(), entity.getOnPos().getY(), entity.getOnPos().getZ());

        // todo: 非强化模式的落雷半径需要缩小
        // todo: 强化模式的落雷半径可以适量增大（2.5？），落雷数量（覆盖面积）需增加

        // 非强化模式
        if (upgradeStatus == 0) {
            for (int wave = 1; wave <= enchantmentLevel*10/2; wave++) {
                TickScheduler.schedule(level, wave * 2, new Runnable() {
                    @Override
                    public void run() {
                        BlockPos randomPos = PositionUtil.getRandomPosInDiamond(targetPos, 2 * enchantmentLevel, level);
                        AttackerProtectLightning.spawnAttackProtectLightningAtGround(level, randomPos, item.owner());
                    }
                });
            }
            return;
        }

        // 强化模式

        // 落雷范围半径 = (附魔等级+1)的2倍
        // 单波落雷数量 = 附魔等级 + 1
        // 总共落雷波数 = 附魔等级 * 2
        for (int wave = 1; wave <= enchantmentLevel*2; wave++) {
            // 10 Tick是生物受伤间隔，保险一点11Tick，保证每次落雷范围的生物都能受到伤害
            TickScheduler.schedule(level, wave*11, new Runnable() {
                @Override
                public void run() {
                    Set<BlockPos> uniqueRandomPosSet = new HashSet<>();
                    for (int spawnCount = 1; spawnCount <= 1 + enchantmentLevel; spawnCount++) {
                        BlockPos randomPos = PositionUtil.getRandomPosInDiamond(targetPos, 2 * enchantmentLevel, level);
                        if (uniqueRandomPosSet.contains(randomPos)) {
                            spawnCount--;
                            continue;
                        }
                        uniqueRandomPosSet.add(new BlockPos(randomPos.getX(), randomPos.getY(), randomPos.getZ()));
                    }
                    for (BlockPos randomPos : uniqueRandomPosSet) {
                        AttackerProtectLightning.spawnAttackProtectLightningAtGround(level, randomPos, item.owner());
                    }
                }
            });
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
