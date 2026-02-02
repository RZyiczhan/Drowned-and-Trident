package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.AttackerProtectLightning.AttackerProtectLightning;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import com.rzero.drownedandtrident.programmingConstant.DefaultEnchantmentUpgradeStatus;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThunderTrajectoryEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final MapCodec<ThunderTrajectoryEnchantment> CODEC = MapCodec.unit(ThunderTrajectoryEnchantment::new);

    public static final ResourceKey<Enchantment> THUNDER_TRAJECTORY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "thunder_trajectory"));
    private static final Logger log = LoggerFactory.getLogger(ThunderTrajectoryEnchantment.class);

    public ThunderTrajectoryEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 1;
        minBaseCost = 20;
        minIncrementCost = 7;
        maxBaseCost = 27;
        maxIncrementCost = 10;
    }

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, THUNDER_TRAJECTORY, Enchantment.enchantment(Enchantment.definition(
                                items.getOrThrow(appliedOnItemType),
                                weight,
                                maxLevel,
                                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                                anvilCost,
                                effectSoltPos)
                        )
                        .withEffect(TridentEnchantmentTriggerTypeRegister.ON_ENTITY_TICK.value(), new ThunderTrajectoryEnchantment())
        );
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {

        ItemStack tridentItem = item.itemStack();

        byte upgradeStatus = tridentItem.getOrDefault(TridentDataComponentRegister.THUNDER_TRAJECTORY_UPGRADE_STATUS, DefaultEnchantmentUpgradeStatus.DEFAULT_THUNDER_TRAJECTORY_UPGRADE_STATUS);


        if (upgradeStatus == 0) {
            //  1）射出后经过1个Tick后落第一发雷，正式开始循环周期（早点落下第一道雷，给用户附魔已生效的快速反馈）
            //  2）平均2.5Tick落一道雷，由于第2.5Tick这种概念技术上不存在，所以是 2Tick后劈第一次，
            //  然后3Tick后劈第二次视为一个标准循环周期，这样平均下来就是5Tick里劈了两次雷，平均2.5Tick一次
//            if (datThrownTrident.getThunderTrajectoryTriggerCoolDownTick() == 2 || datThrownTrident.getThunderTrajectoryTriggerCoolDownTick() == 5)
//                AttackerProtectLightning.spawnAttackProtectLightningAtGround(level, new BlockPos((int) origin.x, (int) origin.y, (int) origin.z), item == null ? null : item.owner());
        } else {
            AttackerProtectLightning.spawnAttackProtectLightningAtGround(level, new BlockPos((int)origin.x, (int)origin.y, (int)origin.z), item == null ? null : item.owner());
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
