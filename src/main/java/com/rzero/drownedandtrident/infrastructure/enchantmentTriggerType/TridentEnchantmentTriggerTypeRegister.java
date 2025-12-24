package com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;


/**
 * 附魔触发类型注册处
 */
public class TridentEnchantmentTriggerTypeRegister {

    public static final DeferredRegister.DataComponents ENTITY_ENCHANTMENT_COMPONENT_TYPE =
            DeferredRegister.createDataComponents(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DrownedandTrident.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedConditionalEffect<EnchantmentEntityEffect>>>>
            POST_HIT = ENTITY_ENCHANTMENT_COMPONENT_TYPE.registerComponentType("post_hit", p_345098_ -> p_345098_.
            persistent(TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>>
            HIT_BLOCK_V2 = ENTITY_ENCHANTMENT_COMPONENT_TYPE.registerComponentType(
            "hit_block_v2", p_350170_ -> p_350170_.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.HIT_BLOCK).listOf())
    );

    public static void register(IEventBus eventBus){
        ENTITY_ENCHANTMENT_COMPONENT_TYPE.register(eventBus);
    }

}
