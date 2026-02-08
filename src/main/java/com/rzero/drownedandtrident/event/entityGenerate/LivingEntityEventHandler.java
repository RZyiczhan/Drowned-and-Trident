package com.rzero.drownedandtrident.event.entityGenerate;

import com.rzero.drownedandtrident.DrownedandTrident;
import com.rzero.drownedandtrident.enchantment.custom.*;
import com.rzero.drownedandtrident.entity.goal.DATBabyDrownedMeleeAttackGoal;
import com.rzero.drownedandtrident.entity.goal.DrownedDATTridentAttackGoal;
import com.rzero.drownedandtrident.programmingModel.DrownedTridentEnchantmentModel;
import com.rzero.drownedandtrident.worldGlobal.WorldPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * 处理生物实体生成时的钩子事件
 */
@EventBusSubscriber(modid = DrownedandTrident.MODID)
public class LivingEntityEventHandler {

    private static List<DrownedTridentEnchantmentModel> customEnchantmentsForLevelOne = new ArrayList<>();
    private static List<DrownedTridentEnchantmentModel> customEnchantmentsForLevelTwo = new ArrayList<>();
    private static Random randomInstance = new Random();

    static {
        customEnchantmentsForLevelOne.add(new DrownedTridentEnchantmentModel(FanSplitEnchantment.FAN_SPLIT, 2, 1, false));
        customEnchantmentsForLevelOne.add(new DrownedTridentEnchantmentModel(ScatterSplitEnchantment.SCATTER_SPLIT, 2, 1, false));
        customEnchantmentsForLevelOne.add(new DrownedTridentEnchantmentModel(ThunderStormEnchantment.THUNDER_STORM, 3, 1, false));
        customEnchantmentsForLevelOne.add(new DrownedTridentEnchantmentModel(ThunderTrajectoryEnchantment.THUNDER_TRAJECTORY, 1, 1, false));
        customEnchantmentsForLevelOne.add(new DrownedTridentEnchantmentModel(ErosionEnchantment.EROSION, 3, 1, false));
    }

    static {
        customEnchantmentsForLevelTwo.add(new DrownedTridentEnchantmentModel(FanSplitEnchantment.FAN_SPLIT, 2, 1, true));
        customEnchantmentsForLevelTwo.add(new DrownedTridentEnchantmentModel(ScatterSplitEnchantment.SCATTER_SPLIT, 2, 1, true));
        customEnchantmentsForLevelTwo.add(new DrownedTridentEnchantmentModel(ThunderStormEnchantment.THUNDER_STORM, 3, 1, true));
        customEnchantmentsForLevelTwo.add(new DrownedTridentEnchantmentModel(ThunderTrajectoryEnchantment.THUNDER_TRAJECTORY, 1, 1, true));
        customEnchantmentsForLevelTwo.add(new DrownedTridentEnchantmentModel(ErosionEnchantment.EROSION, 3, 1, true));
    }

    @SubscribeEvent
    public static void onDrownedJoinLevel(EntityJoinLevelEvent event) {
        // 1. 判断是否是溺尸
        if (event.getEntity() instanceof Drowned drowned) {

            // 2. 只有在服务端才操作
            if (!(drowned.level() instanceof ServerLevel level)) return;

            WorldPhase worldPhaseData = WorldPhase.getWorldPhase(level);
            int worldPhase = worldPhaseData.getCurrentPhase();

            // 3. 增强溺尸装备
            fullyEquipDrowned(drowned, worldPhase);

            // 4. 提高该掉落概率
            // todo : 这个概率应跟随附魔稀有度等级的变化而变化
            drowned.setDropChance(EquipmentSlot.MAINHAND,0.45F);

            // 6. 移除溺尸现有的投掷选择Goal
            drowned.goalSelector.getAvailableGoals().removeIf(new Predicate<WrappedGoal>() {
                @Override
                public boolean test(WrappedGoal wrappedGoal) {
                    return wrappedGoal.getGoal() instanceof RangedAttackGoal && wrappedGoal.getPriority() == 2;
                }
            });

            // 7. 新增自定义的能让溺尸投射自定义三叉戟的选择Goal
            // todo：
            // 1）模型持握方向不对
            //注意：因为mc原设定上小溺尸/小僵尸永远不会长大，所以小溺尸永远不会获得远程投射能力）
            if (!drowned.isBaby()) {
                drowned.goalSelector.addGoal(2,
                        new DrownedDATTridentAttackGoal(drowned, 1.0, 40, 10.0F));
            }

            // 8. 移除溺尸白天非水中不攻击玩家的Goal
            drowned.targetSelector.getAvailableGoals().removeIf(new Predicate<WrappedGoal>() {
                @Override
                public boolean test(WrappedGoal wrappedGoal) {
                    return wrappedGoal.getPriority() == 2;
                }
            });

            // 9. 添加溺尸(包括小溺尸)白天也会无条件攻击玩家的Goal，删除了对是否白天||是否自己在水中的判断
            drowned.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(drowned, Player.class, 10, true, false, new Predicate<LivingEntity>() {
                @Override
                public boolean test(LivingEntity living) {
                    return living != null;
                }
            }));

            // 10. 移除溺尸现有的白天非玩家在水中否则不近战攻击玩家的近战选择Goal
            drowned.goalSelector.getAvailableGoals().removeIf(new Predicate<WrappedGoal>() {
                @Override
                public boolean test(WrappedGoal wrappedGoal) {
                    return wrappedGoal.getGoal() instanceof ZombieAttackGoal && wrappedGoal.getPriority() == 2;
                }
            });

            // 11. 添加溺尸白天也会无条件攻击玩家的近战选择Goal（现在溺尸出生100%自带三叉戟，主要是给被移除了投射选择Goal的小溺尸用的）
            drowned.goalSelector.addGoal(2, new DATBabyDrownedMeleeAttackGoal(drowned, 1.0, false));

            // (可选) 强制赋予它远程攻击的 AI 倾向
            // 原版溺尸会自动检测手持物品切换 AI，所以通常不需要手动改，但可以强制设置一下
            // drowned.setAggressive(true);

        }
    }

    /**
     * 创建溺尸手上的三叉戟w
     * @param drowned
     * @return
     */
    private static void fullyEquipDrowned(Drowned drowned, int worldPhase){

        ItemStack tridentItemStack = new ItemStack(Items.TRIDENT); // 或者 Items.TRIDENT

        RegistryAccess registryAccess = drowned.level().registryAccess();
        HolderLookup.RegistryLookup<Enchantment> enchantRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> enchantment;
        int level;
        if (drowned.isBaby()){
            equipBabyDrowned(drowned, tridentItemStack, enchantRegistry, worldPhase);

        } else {
            DrownedTridentEnchantmentModel drownedTridentEnchantmentModel = customEnchantmentsForLevelOne.get(randomInstance.nextInt(customEnchantmentsForLevelOne.size()));
            enchantment =  enchantRegistry.getOrThrow(drownedTridentEnchantmentModel.getEnchantment());
            // +1 是为了取随机数，java的random的随机最高max-1
            level = randomInstance.nextInt(drownedTridentEnchantmentModel.getMinLevel(), drownedTridentEnchantmentModel.getMaxLevel() + 1);

            tridentItemStack.enchant(enchantment, level);
        }

        drowned.setItemSlot(EquipmentSlot.MAINHAND, tridentItemStack);

        // todo : 这个概率应跟随附魔稀有度等级的变化而变化
        drowned.setDropChance(EquipmentSlot.MAINHAND,0.45F);


    }

    private static void equipBabyDrowned(Drowned drowned, ItemStack tridentItemStack, HolderLookup.RegistryLookup<Enchantment> enchantRegistry, int worldPhase){

        Holder<Enchantment> enchantment = enchantRegistry.getOrThrow(ErosionEnchantment.EROSION);
        int level;

        switch (worldPhase){
            case 1:
                level = 2;
                tridentItemStack.enchant(enchantment, level);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                level = 3;
                tridentItemStack.enchant(enchantment, level);
                equipBabyDrownedWithArmor(drowned);
            case 6:
            default:
                break;
        }
    }

    private static void equipBabyDrownedWithArmor(Drowned drowned){
        Random random = new Random();
        float chance = random.nextFloat();

        if (chance < 0.2){
            drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            drowned.setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            drowned.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            drowned.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
        } else if (chance < 0.4) {
            drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            drowned.setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.DIAMOND_CHESTPLATE));
            drowned.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            drowned.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        } else if (chance < 0.95) {
            drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            drowned.setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.IRON_CHESTPLATE));
            drowned.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            drowned.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        } else {
            drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            drowned.setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.GOLDEN_CHESTPLATE));
            drowned.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
            drowned.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
        }

        drowned.setDropChance(EquipmentSlot.HEAD, 0);
        drowned.setDropChance(EquipmentSlot.BODY, 0);
        drowned.setDropChance(EquipmentSlot.LEGS, 0);
        drowned.setDropChance(EquipmentSlot.FEET, 0);
    }

}
