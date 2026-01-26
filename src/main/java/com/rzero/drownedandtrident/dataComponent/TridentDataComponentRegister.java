package com.rzero.drownedandtrident.dataComponent;

import com.mojang.serialization.Codec;
import com.rzero.drownedandtrident.DrownedandTrident;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TridentDataComponentRegister {

    public static final DeferredRegister<DataComponentType<?>> TRIDENT_DATA_COMPONENT =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DrownedandTrident.MODID);

    // --------------------------------------- 分裂参数 ----------------------------------------------------

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FAN_SPLIT_TICK =
            TRIDENT_DATA_COMPONENT.register("fan_split_tick", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.INT) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FAN_SPLIT_ANGLE =
            TRIDENT_DATA_COMPONENT.register("fan_split_angle", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.INT) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SCATTER_SPLIT_TICK =
            TRIDENT_DATA_COMPONENT.register("scatter_split_tick", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.INT) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SCATTER_SPREAD_LEVEL =
            TRIDENT_DATA_COMPONENT.register("scatter_spread_level", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.INT) // 允许同步给客户端
                    .build());

    // --------------------------------------- 附魔强化状态 ----------------------------------------------------
    // 0 为 未强化
    // 1 为 1级强化
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> SCATTER_SPLIT_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("scatter_split_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> FAN_SPLIT_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("fan_split_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> EROSION_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("erosion_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> EXPLOSIVE_SHOOT_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("explosive_shoot_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> THUNDER_STORM_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("thunder_storm_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> THUNDER_TRAJECTORY_UPGRADE_STATUS =
            TRIDENT_DATA_COMPONENT.register("thunder_trajectory_upgrade_status", () -> DataComponentType.<Byte>builder()
                    .persistent(Codec.BYTE) // 允许保存到磁盘
                    .networkSynchronized(ByteBufCodecs.BYTE) // 允许同步给客户端
                    .build());

    public static void register(IEventBus eventBus){
        TRIDENT_DATA_COMPONENT.register(eventBus);
    }

}
