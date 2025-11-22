package com.rzero.drownedandtrident.datagen;

import com.rzero.drownedandtrident.datagen.provider.ModEnchantmentProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

// 这里必须指定bus，否则默认Bus.Game，无法订阅数据生成event
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){

        DataGenerator dataGenerator = event.getGenerator();

        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();



        ModEnchantmentProvider modEnchantmentProvider = new ModEnchantmentProvider(
                packOutput, lookUpProvider
        );
        dataGenerator.addProvider(true, modEnchantmentProvider);
    }
}
