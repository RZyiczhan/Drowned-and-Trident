package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.event.tickSchedular.TickScheduler;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import com.rzero.drownedandtrident.mixin.mixinInterface.IThrownTridentExt;
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
import net.minecraft.world.entity.projectile.ThrownTrident;
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

        if (entity instanceof ThrownTrident thrownTrident){

            if (!(thrownTrident instanceof IThrownTridentExt thrownTridentMixinExt)) return;

            Set<ResourceKey<Enchantment>> undesiredEnchantment = new HashSet<>();
            undesiredEnchantment.add(Enchantments.LOYALTY);
            undesiredEnchantment.add(SCATTER_SPLIT);

            ItemStack tridentItem = item.itemStack();

            int fanSplitTick = tridentItem.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK);
            int scatterSplitTickTemp = tridentItem.getOrDefault(TridentDataComponentRegister.SCATTER_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK);
            int scatterSpreadLevelTemp = tridentItem.getOrDefault(TridentDataComponentRegister.SCATTER_SPREAD_LEVEL, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPREAD_LEVEL);

            final int scatterSpreadLevel = scatterSpreadLevelTemp;
            final int scatterSplitTick = scatterSplitTickTemp;

            /**
             * 1）由于制造复制体时，无法得知其他分裂行为是否已经执行
             *      所以判断其他分裂行为是否已执行的方式为：该分裂的执行延后tick是否在此分裂之前
             *      以避免出现此分裂产生的克隆三叉戟身上存在着本体已执行但克隆上待执行的分裂附魔
             * 2）游戏逻辑：如果多个分裂在同一Tick执行，则分裂产生的克隆三叉戟上这些同时执行的分裂一个都不带
             */
            if (scatterSplitTick >= fanSplitTick){
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
                            if (thrownTridentMixinExt.drownedandtrident$isHadBeenHit()){
                                return;
                            }
                            ProjectileSplitUtil.generateScatterSplitTrident(level, item.owner(), scatterSplitTick, thrownTrident, stackWithoutNonMigratingEnchantment, scatterSpreadLevel, enchantmentLevel  * 3);
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
