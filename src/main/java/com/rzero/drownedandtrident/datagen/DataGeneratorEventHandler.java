package com.rzero.drownedandtrident.datagen;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.datagen.provider.ModEnchantmentProvider;
import com.rzero.drownedandtrident.datagen.provider.ModItemModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class DataGeneratorEventHandler {

    @SubscribeEvent // on the mod event bus
    public static void gatherData(GatherDataEvent event) {
        // Data generators may require some of these as constructor parameters.
        // See below for more details on each of these.
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Register the provider.
        generator.addProvider(
                // A boolean that determines whether the data should actually be generated.
                // The event provides methods that determine this:
                // event.includeClient(), event.includeServer(),
                // event.includeDev() and event.includeReports().
                // Since recipes are server data, we only run them in a server datagen.
                event.includeServer(),
                // Our provider.
                new ModEnchantmentProvider(output, lookupProvider, DrownedandTrident.MODID)
        );
        // Other data providers here.

        generator.addProvider(
                // A boolean that determines whether the data should actually be generated.
                // The event provides methods that determine this:
                // event.includeClient(), event.includeServer(),
                // event.includeDev() and event.includeReports().
                // Since recipes are server data, we only run them in a server datagen.
                event.includeServer(),
                // Our provider.
                new ModItemModelProvider(output, existingFileHelper, DrownedandTrident.MODID)
        );

    }

}
