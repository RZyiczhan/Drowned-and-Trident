package com.rzero.drownedandtrident.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Set;
import java.util.function.Predicate;

public class ItemStackUtil {

    // 3) 构建一个临时的作为附魔载体不包含不想要的ItemStack
    public static ItemStack buildCopiedSourceTridentWithFilteredEnchantments(ItemStack originalTridentItemStack, Set<ResourceKey<Enchantment>> filterOutEnchantments){
        ItemStack copiedSourceItemStack = originalTridentItemStack.copy();
        ItemEnchantments currentEnchants = copiedSourceItemStack.getOrDefault(DataComponents.ENCHANTMENTS, null);
        if (currentEnchants == null){
            return copiedSourceItemStack;
        }
        ItemEnchantments.Mutable mutableEnchants = new ItemEnchantments.Mutable(currentEnchants);

        mutableEnchants.removeIf(new Predicate<Holder<Enchantment>>() {
            @Override
            public boolean test(Holder<Enchantment> enchantmentHolder) {
                return filterOutEnchantments.contains(enchantmentHolder.getKey());
            }
        });

        copiedSourceItemStack.set(DataComponents.ENCHANTMENTS, mutableEnchants.toImmutable());

        return copiedSourceItemStack;
    }

}
