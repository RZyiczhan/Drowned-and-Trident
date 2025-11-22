package com.rzero.drownedandtrident.enchantment.base;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 每个子类覆写bootstrap来自定义
 */
public abstract class BaseCustomEnchantment {

    protected TagKey<Item> appliedOnItemType;
    protected int weight;
    protected int maxLevel;
    protected int anvilCost;
    protected EquipmentSlotGroup effectSoltPos;
    protected int minBaseCost;
    protected int minIncrementCost;
    protected int maxBaseCost;
    protected int maxIncrementCost;

    public void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                Enchantment.Builder builder){
        registry.register(key, builder.build(key.location()));
    }
}


