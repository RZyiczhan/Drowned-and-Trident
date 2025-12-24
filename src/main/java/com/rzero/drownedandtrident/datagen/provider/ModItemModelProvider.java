package com.rzero.drownedandtrident.datagen.provider;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    private String modId;

    @Override
    protected void registerModels() {
        withExistingParent(DATItemFunctionRegister.DAT_TRIDENT_ITEM.getId().toString(),
                // 指向mc资源的时候用mcLoc，指向自己的资源时用modLoc
                mcLoc("item/handheld"))
                .texture("layer0", mcLoc("item/trident"));
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId) {
        super(output, DrownedandTrident.MODID, existingFileHelper);
        this.modId = modId;
    }
}
