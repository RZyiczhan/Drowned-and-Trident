package com.rzero.drownedandtrident.programmingModel;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 给溺尸手上的三叉戟添加的附魔的属性模型
 */
public class DrownedTridentEnchantmentModel {

    private ResourceKey<Enchantment> enchantment;
    private int maxLevel;
    private int minLevel;

    public ResourceKey<Enchantment> getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(ResourceKey<Enchantment> enchantment) {
        this.enchantment = enchantment;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public DrownedTridentEnchantmentModel(ResourceKey<Enchantment> enchantment, int maxLevel, int minLevel) {
        this.enchantment = enchantment;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
    }
}
