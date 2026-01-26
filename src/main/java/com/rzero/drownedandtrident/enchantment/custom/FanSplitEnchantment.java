package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.event.tickSchedular.TickScheduler;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import com.rzero.drownedandtrident.programmingConstant.DefaultTridentSplitParamConstant;
import com.rzero.drownedandtrident.util.ItemStackUtil;
import com.rzero.drownedandtrident.util.ProjectileSplitUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * 扇形分裂附魔
 */
public class FanSplitEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {
    public static final ResourceKey<Enchantment> FAN_SPLIT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "fan_split"));

    public static final MapCodec<FanSplitEnchantment> CODEC = MapCodec.unit(FanSplitEnchantment::new);
    private static final Logger log = LoggerFactory.getLogger(FanSplitEnchantment.class);

    public FanSplitEnchantment(){
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

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, FAN_SPLIT, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(appliedOnItemType),
                weight,
                maxLevel,
                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                anvilCost,
                effectSoltPos)
        )
                .withEffect(TridentEnchantmentTriggerTypeRegister.AFTER_ENTITY_INIT.get(), new FanSplitEnchantment()));
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (entity instanceof DATThrownTrident datThrownTrident){

            Set<ResourceKey<Enchantment>> undesiredEnchantment = new HashSet<>();
            undesiredEnchantment.add(Enchantments.LOYALTY);
            undesiredEnchantment.add(FAN_SPLIT);

            int fanSplitTickTemp = DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK;
            int fanSplitAngleTemp = DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_ANGLE;
            int scatterSplitTick = DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK;

            if (datThrownTrident.getSplitParam() != null){
                fanSplitTickTemp = datThrownTrident.getSplitParam().fanSplitTick;
                fanSplitAngleTemp = datThrownTrident.getSplitParam().fanSplitAngle;
                scatterSplitTick = datThrownTrident.getSplitParam().scatterSplitTick;
            }

            // 根据强化状态修正真实Tick，这两个附魔未强化的三叉戟，玩家就算通过任何手段获得到了参数被修改的三叉戟也不能应用相关参数
            fanSplitTickTemp = datThrownTrident.getEnchantmentsUpgradeSummary().getFanSplitUpgradeStatus() == 0 ? DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK : fanSplitTickTemp;
            scatterSplitTick = datThrownTrident.getEnchantmentsUpgradeSummary().getScatterSplitUpgradeStatus() == 0 ? DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK : scatterSplitTick;
            fanSplitAngleTemp = datThrownTrident.getEnchantmentsUpgradeSummary().getFanSplitUpgradeStatus() == 0 ? DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_ANGLE : fanSplitAngleTemp;

            final int fanSplitAngle = fanSplitAngleTemp;
            final int fanSplitTick = fanSplitTickTemp;

            if (scatterSplitTick == fanSplitTick) {
                undesiredEnchantment.add(ScatterSplitEnchantment.SCATTER_SPLIT);
            }

            ItemStack stackWithoutNonMigratingEnchantment =
                    ItemStackUtil.buildCopiedSourceTridentWithFilteredEnchantments(item.itemStack(), undesiredEnchantment);


            // delay 支持 0～15 Tick的自由可选 (并推荐1～10，默认0)
            // 角度支持5～15的自由可选（并推荐5～10，默认10）
            TickScheduler.schedule(
                    level,
                    fanSplitTick,
                    new Runnable() {
                        @Override
                        public void run() {
                            if (datThrownTrident.isHadBeenHit()){
                                return;
                            }
                            for (int round = 1; round <= enchantmentLevel; round++){
                                double degree = round * fanSplitAngle;
                                ProjectileSplitUtil.generateFanSplitPairTrident(level, item.owner(), fanSplitTick, datThrownTrident, stackWithoutNonMigratingEnchantment, degree);
                            }
                        }
                    }
            );
        }
    }




    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
