package com.rzero.drownedandtrident.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.enchantment.base.BaseCustomEnchantment;
import com.rzero.drownedandtrident.enchantment.base.BaseEnchantmentDefinition;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.programmingConstant.DefaultEnchantmentUpgradeStatus;
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
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 爆炸射击附魔
 */
public class ExplosiveShootEnchantment extends BaseCustomEnchantment implements EnchantmentEntityEffect, BaseEnchantmentDefinition {

    public static final ResourceKey<Enchantment> EXPLOSIVE_SHOOT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedandTrident.MODID , "explosive_shoot"));

    public static final MapCodec<ExplosiveShootEnchantment> CODEC = MapCodec.unit(ExplosiveShootEnchantment::new);

    public ExplosiveShootEnchantment(){
        anvilCost = 5;
        appliedOnItemType = ItemTags.TRIDENT_ENCHANTABLE;
        weight = 5;
        effectSoltPos = EquipmentSlotGroup.MAINHAND;
        maxLevel = 1;
        minBaseCost = 15;
        minIncrementCost = 10;
        maxBaseCost = 27;
        maxIncrementCost = 10;
    }

    // todo: 如何让三叉戟命中生物也生效，且之后不再生效

    @Override
    public void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        register(context, EXPLOSIVE_SHOOT, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(appliedOnItemType),
                weight,
                maxLevel,
                Enchantment.dynamicCost(minBaseCost, minIncrementCost),
                Enchantment.dynamicCost(maxBaseCost, maxIncrementCost),
                anvilCost,
                effectSoltPos)
                        )
                .withEffect(EnchantmentEffectComponents.HIT_BLOCK, new ExplosiveShootEnchantment())
        );
    }

    // 参考苦力怕的爆炸，普通的radius和强度为3.0F(1级)，高压为6.0F(2级)
    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {


        if (entity.getPersistentData().getBoolean("ExplosiveTriggered")) return;
        entity.getPersistentData().putBoolean("ExplosiveTriggered", true);

        if (!(entity instanceof DATThrownTrident)) return;

        ItemStack datTridentItem = item.itemStack();

        byte upgradeStatus = datTridentItem.getOrDefault(TridentDataComponentRegister.EXPLOSIVE_SHOOT_UPGRADE_STATUS, DefaultEnchantmentUpgradeStatus.DEFAULT_EROSION_UPGRADE_STATUS);

        // 常规：普通苦力怕爆炸
        // 强化：高压苦力怕爆炸
        level.explode(
                item.owner(),
                origin.x, origin.y, origin.z,
                upgradeStatus == 0 ? 3.0F : 6.0F,
                Level.ExplosionInteraction.MOB
        );

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
