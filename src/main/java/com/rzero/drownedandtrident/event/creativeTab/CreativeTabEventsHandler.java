package com.rzero.drownedandtrident.event.creativeTab;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class CreativeTabEventsHandler {

    /**
     * Trigger when open the creative mode package UI
     * Change MC origin trident to mod trident
     * @param event
     */
    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        // 1. 检查是否是战斗栏 (COMBAT) 或者 搜索栏 (SEARCH)
        // 原版三叉戟主要存在于这两个地方。如果你只想修改战斗栏，去掉 SEARCH 的判断即可。
        if (event.getTabKey() == CreativeModeTabs.COMBAT || event.getTabKey() == CreativeModeTabs.SEARCH) {

            // 获取当前选项卡的所有条目
            ObjectSortedSet<ItemStack> parentEntries = event.getParentEntries();

            for (ItemStack itemStack : parentEntries){
                if (itemStack.getItem() == Items.TRIDENT){
                    ItemStack customTridentStack = new ItemStack(DATItemFunctionRegister.DAT_TRIDENT_ITEM.get());
                    event.insertAfter(itemStack, customTridentStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    event.remove(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    break;
                }
            }
        }
    }
}