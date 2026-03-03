package com.rzero.drownedandtrident.modRegistry.menu;

import com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable.TridentEnchantmentMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenu {

    public static final Supplier<MenuType<TridentEnchantmentMenu>> TRIDENT_ENCHANTMENT_MENU = ModMenuFunctionRegister.MOD_MENU.register(
            "trident_enchantment",
            () -> new MenuType<>(
                    (containerId, playerInventory) -> new TridentEnchantmentMenu(containerId, playerInventory, ContainerLevelAccess.NULL),
                    FeatureFlags.DEFAULT_FLAGS
            )
    );

}
