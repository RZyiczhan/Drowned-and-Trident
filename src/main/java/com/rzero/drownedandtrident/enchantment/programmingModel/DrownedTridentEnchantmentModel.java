package com.rzero.drownedandtrident.enchantment.programmingModel;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 给溺尸手上的三叉戟添加的附魔的属性模型
 */
public class DrownedTridentEnchantmentModel {

    private ResourceKey<Enchantment> enchantment;
    private int maxLevel;
    private int minLevel;
    private boolean hadUpgrade;

    public ResourceKey<Enchantment> getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(ResourceKey<Enchantment> enchantment) {
        this.enchantment = enchantment;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isHadUpgrade(){ return hadUpgrade;}

    public DrownedTridentEnchantmentModel(ResourceKey<Enchantment> enchantment, int maxLevel, int minLevel, boolean hadUpgrade) {
        this.enchantment = enchantment;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
        this.hadUpgrade = hadUpgrade;
    }
}
