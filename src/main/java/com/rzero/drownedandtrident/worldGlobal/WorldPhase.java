package com.rzero.drownedandtrident.worldGlobal;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WorldPhase extends SavedData {

    public WorldPhase(){};

    private static final String FILE_NAME = "DrownedAndTrident_phases";

    // 1) 1阶段：没打过任何Boss
    // 2）2阶段：击败“溺尸化身”
    // 3）3阶段：击败“溺尸王”
    // 4）4阶段：击败“勇猛溺尸”
    // 5）5阶段：击败“腐化的深海之眼”
    // 6）6阶段：击败最终Boss，“the starter”
    private int currentPhase = 1;

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("drownedAndTrident:worldPhase", currentPhase);
        return null;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
        this.setDirty(); // 告诉 Minecraft 数据变了，需要存盘
    }

    public static WorldPhase getWorldPhase(ServerLevel level){

        // 哪怕是在非主世界调用，我们也通常获取主世界（Overworld）的数据，实现全服同步
        ServerLevel overworld = level.getServer().overworld();
        DimensionDataStorage storage = overworld.getDataStorage();

        SavedData.Factory<WorldPhase> factory = new SavedData.Factory<>(
                WorldPhase::new,
                WorldPhase::load,
                DataFixTypes.LEVEL // 或者 null，如果你不需要版本迁移
        );

        return storage.computeIfAbsent(factory, FILE_NAME);

    }

    // 必须：从 NBT 读取数据的工厂方法
    public static WorldPhase load(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        WorldPhase data = new WorldPhase();
        // 这里不需要 if，因为默认值是 0/false，如果是旧存档没 key 也没关系
        if (tag.contains("drownedAndTrident:worldPhase"))
            data.currentPhase = tag.getInt("drownedAndTrident:worldPhase");
        return data;
    }

}
