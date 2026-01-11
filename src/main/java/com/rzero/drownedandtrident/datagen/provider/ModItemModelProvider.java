package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    private String modId;

    @Override
    protected void registerModels() {
//        withExistingParent(DATItemFunctionRegister.DAT_TRIDENT_ITEM.getId().toString(),
//                // 指向mc资源的时候用mcLoc，指向自己的资源时用modLoc
//                mcLoc("builtin/entity"))
//                .texture("layer0", mcLoc("item/trident"));
//

        String name = BuiltInRegistries.ITEM.getKey(DATItemFunctionRegister.DAT_TRIDENT_ITEM.get()).getPath();

        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("minecraft:builtin/entity"))
                .texture("layer0", "minecraft:item/trident");

//        existingFileHelper.trackGenerated(
//                mcLoc("builtin/entity"),
//                net.neoforged.neoforge.client.model.generators.ModelProvider.MODEL
//        );
//
//        withExistingParent(
//                DATItemFunctionRegister.DAT_TRIDENT_ITEM.getId().getPath(),
//                mcLoc("builtin/entity")
//        ).texture("layer0", mcLoc("item/trident"));
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId) {
        super(output, DrownedandTrident.MODID, existingFileHelper);
        this.modId = modId;
    }
}
