package com.rzero.drownedandtrident.event.entityGenerate;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.custom.*;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import com.rzero.drownedandtrident.programmingModel.DrownedTridentEnchantmentModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 处理生物实体生成时的钩子事件
 */
@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class LivingEntityEventHandler {

    private static List<DrownedTridentEnchantmentModel> customEnchantments = new ArrayList<>();
    private static Random randomInstance = new Random();

    static {
        customEnchantments.add(new DrownedTridentEnchantmentModel(FanSplitEnchantment.FAN_SPLIT, 2, 1));
        customEnchantments.add(new DrownedTridentEnchantmentModel(ExplosiveShootEnchantment.EXPLOSIVE_SHOOT, 1, 1));
        customEnchantments.add(new DrownedTridentEnchantmentModel(ScatterSplitEnchantment.SCATTER_SPLIT, 2, 1));
        customEnchantments.add(new DrownedTridentEnchantmentModel(ThunderStormEnchantment.THUNDER_STORM, 3, 1));
        customEnchantments.add(new DrownedTridentEnchantmentModel(ThunderTrajectoryEnchantment.THUNDER_TRAJECTORY, 1, 1));
        customEnchantments.add(new DrownedTridentEnchantmentModel(ErosionEnchantment.EROSION, 3, 1));
    }

    @SubscribeEvent
    public static void onDrownedJoinLevel(EntityJoinLevelEvent event) {
        // 1. 判断是否是溺尸
        if (event.getEntity() instanceof Drowned drowned) {

            // 2. 只有在服务端才操作
            if (drowned.level().isClientSide) return;

            // 3. 创建三叉戟并附加附魔
            ItemStack datTrident = createDrownedUseTrident(drowned);

            // 4. 装备给溺尸
            drowned.setItemSlot(EquipmentSlot.MAINHAND, datTrident);

            // 5. 提高该掉落概率
            // todo : 这个概率应跟随附魔稀有度等级的变化而变化
            drowned.setDropChance(EquipmentSlot.MAINHAND,0.45F);

            // (可选) 强制赋予它远程攻击的 AI 倾向
            // 原版溺尸会自动检测手持物品切换 AI，所以通常不需要手动改，但可以强制设置一下
//            drowned.setAggressive(true);

        }
    }

    /**
     * 创建溺尸手上的三叉戟w
     * @param drowned
     * @return
     */
    private static ItemStack createDrownedUseTrident(Drowned drowned){

        ItemStack datTrident = new ItemStack(DATItemFunctionRegister.DAT_TRIDENT_ITEM.get()); // 或者 Items.TRIDENT

        RegistryAccess registryAccess = drowned.level().registryAccess();
        HolderLookup.RegistryLookup<Enchantment> enchantRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> enchantment;
        int level;
        if (drowned.isBaby()){
            // 小溺尸一律用侵蚀II三叉戟
            enchantment = enchantRegistry.getOrThrow(ErosionEnchantment.EROSION);
            level = 2;
        } else {
            DrownedTridentEnchantmentModel drownedTridentEnchantmentModel = customEnchantments.get(randomInstance.nextInt(customEnchantments.size()));
            enchantment =  enchantRegistry.getOrThrow(drownedTridentEnchantmentModel.getEnchantment());
            // +1 是为了取随机数，java的random的随机最高max-1
            level = randomInstance.nextInt(drownedTridentEnchantmentModel.getMinLevel(), drownedTridentEnchantmentModel.getMaxLevel() + 1);
        }

        datTrident.enchant(enchantment, level);

        return datTrident;
    }

}
