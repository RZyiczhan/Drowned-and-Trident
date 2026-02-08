package com.rzero.drownedandtrident.mixin.mixinImpl;

import com.rzero.drownedandtrident.enchantment.custom.ErosionEnchantment;
import com.rzero.drownedandtrident.entity.addition.DrownedAddition;
import com.rzero.drownedandtrident.programmingModel.DrownedTridentEnchantmentModel;
import com.rzero.drownedandtrident.worldGlobal.WorldPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * 溺尸生成时，劫持其生成逻辑，为其上装备+更换Goal
 * */
@Mixin(Drowned.class)
public class DrownedMixin {

    private static final Logger log = LoggerFactory.getLogger(DrownedMixin.class);

    /**
     * 溺尸生成时，劫持其生成逻辑，为其上装备
     * */
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void onPopulateDefaultEquipmentSlotsEnd(CallbackInfo ci){

        Drowned drowned = (Drowned)(Object)this;

        // 检查是不是客户端，通常逻辑只在服务端跑
        if (!(drowned.level() instanceof ServerLevel level)) return;

        WorldPhase worldPhaseData = WorldPhase.getWorldPhase(level);
        int worldPhase = worldPhaseData.getCurrentPhase();

        // 3. 增强溺尸装备
        fullyEquipDrowned(drowned, worldPhase);


    }


    /**
     * 溺尸生成时，劫持其Goal逻辑，更换Goal？确认下执行的目的
     * */
//    @Inject(method = "shootFromRotation", at = @At("TAIL"))
//    private void onShootFromRotationForThrownTrident(CallbackInfo ci){
//
//        WorldPhase worldPhaseData = WorldPhase.getWorldPhase(level);
//        int worldPhase = worldPhaseData.getCurrentPhase();
//
//        // 3. 增强溺尸装备
//        fullyEquipDrowned(drowned, worldPhase);
//
//
//        Projectile projectile = (Projectile) (Object) this;
//        // 只作用于三叉戟
//        if (!(projectile instanceof ThrownTrident thrownTrident)) return;
//
//        // 检查是不是客户端，通常逻辑只在服务端跑
//        if (!(thrownTrident.level() instanceof ServerLevel serverlevel)) {
//            return;
//        }
//
//        ModEnchantmentHelper.doAfterEntityInit(serverlevel, thrownTrident, thrownTrident.getWeaponItem(), thrownTrident.getEyePosition(),
//                thrownTrident.getOwner() instanceof LivingEntity owner ? owner : null);
//
//    }
//
//    addBehaviourGoals


    /**
     * 创建溺尸手上的三叉戟w
     * @param drowned
     * @return
     */
    private void fullyEquipDrowned(Drowned drowned, int worldPhase){

        ItemStack tridentItemStack = new ItemStack(Items.TRIDENT); // 或者 Items.TRIDENT

        RegistryAccess registryAccess = drowned.level().registryAccess();
        HolderLookup.RegistryLookup<Enchantment> enchantRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> enchantment;
        int level;
        if (drowned.isBaby()){
            equipBabyDrowned(drowned, tridentItemStack, enchantRegistry, worldPhase);

        } else {
            DrownedTridentEnchantmentModel drownedTridentEnchantmentModel = DrownedAddition.customEnchantmentsForLevelOne.get(DrownedAddition.randomInstance.nextInt(DrownedAddition.customEnchantmentsForLevelOne.size()));
            enchantment =  enchantRegistry.getOrThrow(drownedTridentEnchantmentModel.getEnchantment());
            // +1 是为了取随机数，java的random的随机最高max-1
            level = DrownedAddition.randomInstance.nextInt(drownedTridentEnchantmentModel.getMinLevel(), drownedTridentEnchantmentModel.getMaxLevel() + 1);

            tridentItemStack.enchant(enchantment, level);
        }

        drowned.setItemSlot(EquipmentSlot.MAINHAND, tridentItemStack);

        // todo : 这个概率应跟随附魔稀有度等级的变化而变化
        drowned.setDropChance(EquipmentSlot.MAINHAND,0.45F);


    }

    private void equipBabyDrowned(Drowned drowned, ItemStack tridentItemStack, HolderLookup.RegistryLookup<Enchantment> enchantRegistry, int worldPhase){

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

    private void equipBabyDrownedWithArmor(Drowned drowned){
        float chance = DrownedAddition.randomInstance.nextFloat();

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
