package com.rzero.drownedandtrident.item.override.DATTridentItem;

import com.rzero.drownedandtrident.dataComponent.TridentDataComponentRegister;
import com.rzero.drownedandtrident.entity.override.DATThrownTrident.DATThrownTrident;
import com.rzero.drownedandtrident.programmingConstant.DefaultTridentSplitParamConstant;
import com.rzero.drownedandtrident.programmingModel.TridentSplitParamModel;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DATTridentItem extends TridentItem {
    public DATTridentItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i >= 10) {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                if (!(f > 0.0F) || player.isInWaterOrRain()) {
                    if (!isTooDamagedToUse(stack)) {
                        Holder<SoundEvent> holder = EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND)
                                .orElse(SoundEvents.TRIDENT_THROW);
                        if (!level.isClientSide) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                            if (f == 0.0F) {
                                // 实现的变更：
                                // 1）把这里的原版ThrownTrident替换成了自定义的DATThrownTrident
                                // 2）发射方法换成了自定义的
                                DATThrownTrident datThrownTrident = new DATThrownTrident(
                                        level, player, stack, new TridentSplitParamModel(
                                                stack.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_ANGLE, DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_ANGLE),
                                                stack.getOrDefault(TridentDataComponentRegister.FAN_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_FAN_SPLIT_TICK),
                                                stack.getOrDefault(TridentDataComponentRegister.SCATTER_SPREAD_LEVEL, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPREAD_LEVEL),
                                                stack.getOrDefault(TridentDataComponentRegister.SCATTER_SPLIT_TICK, DefaultTridentSplitParamConstant.DEFAULT_SCATTER_SPLIT_TICK)
                                ));
                                ServerLevel serverLevel = (ServerLevel) level;
                                datThrownTrident.shootFromRotation(player, player.getXRot(), player.getYRot(),
                                        0.0F, 2.5F, 1F,
                                        serverLevel, new Vec3(player.getX(), player.getY(), player.getZ()),
                                        stack);
                                if (player.hasInfiniteMaterials()) {
                                    datThrownTrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                }
                                level.addFreshEntity(datThrownTrident);
                                level.playSound(null, datThrownTrident, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                if (!player.hasInfiniteMaterials()) {
                                    player.getInventory().removeItem(stack);
                                }
                            }
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        if (f > 0.0F) {
                            float f7 = player.getYRot();
                            float f1 = player.getXRot();
                            float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                            float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
                            float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                            float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                            f2 *= f / f5;
                            f3 *= f / f5;
                            f4 *= f / f5;
                            player.push((double)f2, (double)f3, (double)f4);
                            player.startAutoSpinAttack(20, 8.0F, stack);
                            if (player.onGround()) {
                                float f6 = 1.1999999F;
                                player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                            }

                            level.playSound(null, player, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }



}
