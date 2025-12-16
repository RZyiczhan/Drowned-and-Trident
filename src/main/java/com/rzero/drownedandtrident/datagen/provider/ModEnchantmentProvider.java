package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.enchantment.ModEnchantmentBootstrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, new ModEnchantmentBootstrapper());

    public ModEnchantmentProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, registries, registrySetBuilder, Set.of(modId));
    }

}
