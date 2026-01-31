package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.event.tickSchedular.TickScheduler;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import com.rzero.drownedandtrident.programmingConstant.DefaultEnchantmentUpgradeStatus;
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

import java.util.HashSet;
import java.util.Set;

/**
 * 散射裂解附魔
 */
public class ScatterSplitEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final ResourceKey<Enchantment> SCATTER_SPLIT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "scatter_split"));

    public static final MapCodec<ScatterSplitEnchantment> CODEC = MapCodec.unit(ScatterSplitEnchantment::new);

    public ScatterSplitEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 3;
        minBaseCost = 1;
        minIncrementCost = 1;
        maxBaseCost = 2;
        maxIncrementCost = 3;
    }

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context,SCATTER_SPLIT, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(appliedOnItemType),
                        weight,
                        maxLevel,
                        Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                        Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                        anvilCost,
                        effectSoltPos)
                )
                .withEffect(TridentEnchantmentTriggerTypeRegister.AFTER_ENTITY_INIT.get(), new ScatterSplitEnchantment()));
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        if (entity instanceof DATThrownTrident datThrownTrident){

            Set<ResourceKey<Enchantment>> undesiredEnchantment = new HashSet<>();
            undesiredEnchantment.add(Enchantments.LOYALTY);
            undesiredEnchantment.add(SCATTER_SPLIT);

            ItemStack datTridentItem = item.itemStack();

            int fanSplitTick = datTridentItem.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK);
            int scatterSplitTickTemp = datTridentItem.getOrDefault(TridentDataComponentRegister.SCATTER_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK);
            int scatterSpreadLevelTemp = datTridentItem.getOrDefault(TridentDataComponentRegister.SCATTER_SPREAD_LEVEL, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPREAD_LEVEL);

            byte fanSplitUpgradeStatus = datTridentItem.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_UPGRADE_STATUS, DefaultEnchantmentUpgradeStatus.DEFAULT_FAN_SPLIT_UPGRADE_STATUS);
            byte scatterSplitUpgradeStatus = datTridentItem.getOrDefault(TridentDataComponentRegister.SCATTER_SPLIT_UPGRADE_STATUS, DefaultEnchantmentUpgradeStatus.DEFAULT_SCATTER_SPLIT_UPGRADE_STATUS);

            // 根据强化状态修正真实Tick，这两个附魔未强化的三叉戟，玩家就算通过任何手段获得到了参数被修改的三叉戟也不能应用相关参数
            if (fanSplitUpgradeStatus == 0){
                fanSplitTick = DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK;
            }
            if (scatterSplitUpgradeStatus == 0){
                scatterSplitTickTemp = DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK;
                scatterSpreadLevelTemp = DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPREAD_LEVEL;
            }

            final int scatterSpreadLevel = scatterSpreadLevelTemp;
            final int scatterSplitTick = scatterSplitTickTemp;

            if (scatterSplitTick == fanSplitTick){
                undesiredEnchantment.add(FanSplitEnchantment.FAN_SPLIT);
            }

            ItemStack stackWithoutNonMigratingEnchantment =
                    ItemStackUtil.buildCopiedSourceTridentWithFilteredEnchantments(item.itemStack(), undesiredEnchantment);

            // delay 支持 0～15 Tick的自由可选 (并推荐1～10，默认0)
            // 角度支持5～15的自由可选（并推荐5～10，默认10）
            TickScheduler.schedule(
                    level,
                    scatterSplitTick,
                    new Runnable() {
                        @Override
                        public void run() {
                            if (datThrownTrident.isHadBeenHit()){
                                return;
                            }
                            ProjectileSplitUtil.generateScatterSplitTrident(level, item.owner(), scatterSplitTick, datThrownTrident, stackWithoutNonMigratingEnchantment, scatterSpreadLevel, enchantmentLevel  * 3);
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
