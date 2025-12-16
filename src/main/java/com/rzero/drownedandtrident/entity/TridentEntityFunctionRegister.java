package com.rzero.drownedandtrident.entity;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.entity.override.AttackerProtectLightning.AttackerProtectLightning;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 自定义Entity功能的注册器
 */
public class TridentEntityFunctionRegister {

    /**
     * Override(伪造代替）物品的注册器
     */
    public static final DeferredRegister<EntityType<?>> OVERRIDE_ENTITY_TYPE =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DrownedandTrident.MODID);

    /**
     * 其他自定义物品的注册器
     */
    public static final DeferredRegister<EntityType<?>> CUSTOM_ENTITY_TYPE =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DrownedandTrident.MODID);

    public static final Supplier<EntityType<AttackerProtectLightning>> ATTACK_PROTECT_LIGHTNING =
            OVERRIDE_ENTITY_TYPE.register("attack_protect_lightning", () -> AttackerProtectLightning.ATTACK_PROTECT_LIGHTNING);

    public static final Supplier<EntityType<DATThrownTrident>> DAT_THROWN_TRIDENT =
            OVERRIDE_ENTITY_TYPE.register("dat_thrown_trident", () -> DATThrownTrident.DAT_THROWN_TRIDENT);


    public static void register(IEventBus eventBus){
        CUSTOM_ENTITY_TYPE.register(eventBus);
        OVERRIDE_ENTITY_TYPE.register(eventBus);
    }



}
