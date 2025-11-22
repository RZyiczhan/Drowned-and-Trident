package com.rzero.drownedandtrident.enchantment;

import com.rzero.drownedandtrident.DrownedAndTridentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> THUNDER_STORM = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(DrownedAndTridentMod.MODID , "thunder_storm"));

    public static void bootstrap(BootstrapContext<Enchantment> context){
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, THUNDER_STORM, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                5,
                2,
                Enchantment.dynamicCost(1, 1),
                Enchantment.dynamicCost(2, 3),
                5,
                EquipmentSlotGroup.MAINHAND)
        ).withEffect(EnchantmentEffectComponents.HIT_BLOCK, new ThunderStormEnchantment()) );
    }


    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builder){
        registry.register(key, builder.build(key.location()));
    }

}
