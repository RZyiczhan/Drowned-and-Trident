package com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class  ModEnchantmentHelper {

    private static final Logger log = LoggerFactory.getLogger(ModEnchantmentHelper.class);

    public static void onEntityTick(ServerLevel serverLevel, Entity entity, ItemStack entityCreatorItemSource, Vec3 currPos, LivingEntity entityCreator){
        runIterationOnItem(
                entityCreatorItemSource,
                new EnchantmentVisitor() {
                    @Override
                    public void accept(Holder<Enchantment> enchantment, int level) {
                        ModDefinedEnchantmentTriggerFunction.onEntityTick(serverLevel, entity, enchantment.value(), currPos, entityCreatorItemSource, entityCreator, level);
                    }
                }
        );
    }


    public static void doAfterEntityInit(ServerLevel serverLevel, Entity entity, ItemStack creatorStack, Vec3 shootPos, LivingEntity entityCreator){
        runIterationOnItem(
                creatorStack,
                new EnchantmentVisitor() {
                    @Override
                    public void accept(Holder<Enchantment> enchantment, int level) {
                        ModDefinedEnchantmentTriggerFunction.doAfterEntityInit(serverLevel, level, entity, enchantment.value(), shootPos, creatorStack, entityCreator) ;
                    }
                }
        );
    }

    /**
     * copy from EnchantmentHelper，作用，依次遍历物品上的每一个附魔，应用指定附魔触发器的触发方法
     * @param stack
     * @param visitor
     */
    public static void runIterationOnItem(ItemStack stack, ModEnchantmentHelper.EnchantmentVisitor visitor) {
        // 从itemStack上获取到了item上绑定的所有附魔
        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        // NeoForge 允许模组通过代码动态改变一个物品的附魔列表，而不需要真的把附魔写入物品的 NBT 数据里。（来自gemini 3 pro的解释）
        // Neo: Respect gameplay-only enchantments when doing iterations
        var lookup = net.neoforged.neoforge.common.CommonHooks.resolveLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
        if (lookup != null) {
            itemenchantments = stack.getAllEnchantments(lookup);
        }

        // 对获取到的物品上的每一个附魔，应用指定附魔触发器的触发方法
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            visitor.accept(entry.getKey(), entry.getIntValue());
        }
    }


    @FunctionalInterface
    public interface EnchantmentInSlotVisitor {
        void accept(Holder<Enchantment> enchantment, int level, EnchantedItemInUse item);
    }

    /**
     * copy from EnchantmentHelper
     */
    @FunctionalInterface
    public interface EnchantmentVisitor {
        void accept(Holder<Enchantment> enchantment, int level);
    }



    public static EnchantmentInstance selectDATTridentEnchantment(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments) {
        EnchantmentInstance enchantmentInstance = null;
        Item item = stack.getItem();
        int i = stack.getEnchantmentValue();
        if (i <= 0) {
            return null;
        } else {
            // 得清楚这个level是什么
            // 已知这个level会随着书架的变化而变化
            level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
            level = Mth.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE);

            List<EnchantmentInstance> list1 = getAvailableEnchantmentResults(level, stack, possibleEnchantments);


            if (!list1.isEmpty()) {
                // MC原逻辑：从可选附魔的池子中根据每个附魔的weight权重抽取出
                Optional<EnchantmentInstance> optionalEnchantmentInstance = WeightedRandom.getRandomItem(random, list1);
                enchantmentInstance = optionalEnchantmentInstance.isPresent() ? optionalEnchantmentInstance.get() : null;
            }

            return enchantmentInstance;
        }
    }

    private static List<EnchantmentInstance> getAvailableEnchantmentResults(int level, ItemStack stack, Stream<Holder<Enchantment>> possibleEnchantments) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        boolean flag = stack.is(Items.BOOK);

        ItemEnchantments currentExistingEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        log.warn("level {}", level);

        // Neo: Rewrite filter logic to call isPrimaryItemFor instead of hardcoded vanilla logic.
        // The original logic is recorded in the default implementation of IItemExtension#isPrimaryItemFor.
        possibleEnchantments.filter(stack::isPrimaryItemFor).forEach(enchantmentHolder -> {
            Enchantment enchantment = enchantmentHolder.value();

            for (int enchantmentLevel = enchantment.getMaxLevel(); enchantmentLevel >= enchantment.getMinLevel(); enchantmentLevel--) {

                // 能被列入附魔台该级附魔的选项需满足三个条件：（前两个的条件是mc原逻辑）
                // 1）基于书架数随机出的level >= 附魔的该附魔等级的minCost
                // 2）基于书架数随机出的level <= 附魔的该附魔等级的maxCost (再思考一下这条需要的必要性)
                // 3）最大不大于玩家三叉戟上附魔等级+1, 如果玩家当前没有该附魔，则该附魔最高为1级

                if (level >= enchantment.getMinCost(enchantmentLevel) && level <= enchantment.getMaxCost(enchantmentLevel)) {
                    boolean alreadyContainThisEnchantment = currentExistingEnchantments.keySet().contains(enchantmentHolder);
                    if (alreadyContainThisEnchantment && currentExistingEnchantments.getLevel(enchantmentHolder) + 1 == enchantmentLevel
                            || !alreadyContainThisEnchantment && enchantmentLevel == 1){

//                        log.warn("addedAvailableInstance: {}; with enchantment level {}; alreadyContainThisEnchantment: {}", enchantment.toString(), enchantmentLevel, alreadyContainThisEnchantment);
//                        if (alreadyContainThisEnchantment){
//                            log.error("currentEnchantmentLevel : {}", currentExistingEnchantments.getLevel(enchantmentHolder));
//                        }

                        list.add(new EnchantmentInstance((Holder<Enchantment>)enchantmentHolder, enchantmentLevel));
                        break;
                    }
                }
            }
        });
        return list;
    }

}
