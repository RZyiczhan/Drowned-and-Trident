package com.rzero.drownedandtrident.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class ModLootTableProvider implements LootTableSubProvider {

    public ModLootTableProvider(HolderLookup.Provider lookupProvider) {
        // Unlike with blocks, we do not provide a set of known entity types. Vanilla instead uses custom checks here.
        super();
    }


    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

    }
}
