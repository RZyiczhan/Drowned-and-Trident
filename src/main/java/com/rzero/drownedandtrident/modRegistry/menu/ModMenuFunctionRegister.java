package com.rzero.drownedandtrident.modRegistry.menu;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.modRegistry.block.DATBlock;
import com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable.TridentEnchantmentMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuFunctionRegister {

    public static final DeferredRegister<MenuType<?>> MOD_MENU = DeferredRegister.create(Registries.MENU, DrownedandTrident.MODID);

    public static void register(IEventBus eventBus) throws ClassNotFoundException {
        // 初始化Block定义类，保证定义类被使用，来确保其static变量被正常初始化以达成注册
        Class.forName(ModMenu.class.getName());
        MOD_MENU.register(eventBus);
    }

}
