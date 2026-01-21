package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.event.tickSchedular.TickScheduler;
import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.TridentEnchantmentTriggerTypeRegister;
import com.rzero.drownedandtrident.util.ItemStackUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
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

            ItemStack stackWithoutNonMigratingEnchantment =
                    ItemStackUtil.buildCopiedSourceTridentWithFilteredEnchantments(item.itemStack(), undesiredEnchantment);

            // todo：问题1，如何空中获得分裂三叉戟所在的pos
            // todo：问题2，投射时就分裂的情况，三叉戟的生成位置在脚下，而不是靠近头顶的实际抛射位置（还是因为传入的origin是玩家所在的XYZ，而不是三叉戟的）


            // delay 支持 1～10 Tick的自由可选
            TickScheduler.schedule(
                    level,
                    0,
                    new Runnable() {
                        @Override
                        public void run() {
                            generateSplitTrident(level, item.owner(), datThrownTrident, stackWithoutNonMigratingEnchantment);
                        }
                    }
            );
            // todo : 拿着stackWithoutNonMigratingEnchantment和currentVectorWithVelocity生成新角度下的三叉戟

        }

    }

    // todo：貌似生成的三叉戟的投掷朝向比原三叉戟高一点

    private void generateSplitTrident(ServerLevel level, LivingEntity tridentOwner, DATThrownTrident originDATThrownTrident, ItemStack stackWithoutNonMigratingEnchantment){
        float angleRadians = (float) Math.toRadians(15);

        Vec3 currentVectorWithVelocity = originDATThrownTrident.getDeltaMovement();
//        Vec3 splitPos = originDATThrownTrident.getPosition(1);
        Vec3 splitPos = originDATThrownTrident.getEyePosition();


        Vec3 leftDelta = currentVectorWithVelocity.yRot(angleRadians);  // 向左偏（假设）
        Vec3 rightDelta = currentVectorWithVelocity.yRot(-angleRadians);

        DATThrownTrident cloneThrownTrident = new DATThrownTrident(level, tridentOwner, stackWithoutNonMigratingEnchantment);
        cloneThrownTrident.setDeltaMovement(leftDelta);

        // 投射就分裂时，初始发射位置有问题，从脚底下，而不是头上

        cloneThrownTrident.setPos(splitPos);
        cloneThrownTrident.pickup = AbstractArrow.Pickup.DISALLOWED;

        // G. (可选) 强制同步一下实体的朝向，让模型看起来也是歪着飞的
        // 虽然 Projectile.tick() 会自动更新，但手动设置一下更丝滑
        double d0 = leftDelta.horizontalDistance();
        cloneThrownTrident.setYRot((float)(Math.atan2(leftDelta.x, leftDelta.z) * (double)(180F / (float)Math.PI)));
        cloneThrownTrident.setXRot((float)(Math.atan2(leftDelta.y, d0) * (double)(180F / (float)Math.PI)));
        cloneThrownTrident.yRotO = cloneThrownTrident.getYRot();
        cloneThrownTrident.xRotO = cloneThrownTrident.getXRot();

        // H. 加入世界
        level.addFreshEntity(cloneThrownTrident);
    }


    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
