package com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType;

import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.UnaryOperator;


/**
 * 附魔触发类型注册处
 */
public class TridentEnchantmentTriggerTypeRegister {

    public static final DeferredRegister.DataComponents ENTITY_ENCHANTMENT_COMPONENT_TYPE =
            DeferredRegister.createDataComponents(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DrownedandTrident.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>>
            ON_ENTITY_INIT = ENTITY_ENCHANTMENT_COMPONENT_TYPE.registerComponentType("on_entity_init", new UnaryOperator<DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>>>() {
        @Override
        public DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> apply(DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> listBuilder) {
            return listBuilder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.EMPTY).listOf());
        }
    });

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>>
            AFTER_ENTITY_INIT = ENTITY_ENCHANTMENT_COMPONENT_TYPE.registerComponentType("after_entity_init", new UnaryOperator<DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>>>() {
        @Override
        public DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> apply(DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> listBuilder) {
            return listBuilder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.EMPTY).listOf());
        }
    });

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>>
            ON_ENTITY_TICK = ENTITY_ENCHANTMENT_COMPONENT_TYPE.registerComponentType("on_entity_tick", new UnaryOperator<DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>>>() {
        @Override
        public DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> apply(DataComponentType.Builder<List<ConditionalEffect<EnchantmentEntityEffect>>> listBuilder) {
            return listBuilder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.EMPTY).listOf());
        }
    });

    public static void register(IEventBus eventBus){
        ENTITY_ENCHANTMENT_COMPONENT_TYPE.register(eventBus);
    }

}
