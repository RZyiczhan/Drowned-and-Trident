package com.rzero.drownedandtrident.entity.goal;

import com.rzero.drownedandtrident.entity.addition.DrownedAddition;
import com.rzero.drownedandtrident.entity.goal.base.DATRangeAttackGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Items;

/**
 * 可以让溺尸远程使用自定义三叉戟的选择Goal，覆盖掉DrownedTridentAttackGoal
 */
public class DrownedDATTridentAttackGoal extends DATRangeAttackGoal {

    private final Drowned drowned;

    // todo：
    // DrownedDATTridentAttackGoal的Tick目前直接用的RangedAttackGoal的Tick
    // RangedAttackGoal的Tick内存在this.rangedAttackMob.performRangedAttack(this.target, f1);
    // 这个会导到Drowned实现的performRangedAttack，其中生成三叉戟的逻辑是
    // ThrownTrident throwntrident = new ThrownTrident(this.level(), this, new ItemStack(Items.TRIDENT));
    // 要想办法把这个盖掉


    public DrownedDATTridentAttackGoal(RangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
        super(rangedAttackMob, speedModifier, attackInterval, attackRadius);
        this.drowned = (Drowned)rangedAttackMob;
    }
    @Override
    public boolean canUse() {
        return super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT);
    }

    @Override
    public void start() {
        super.start();
        this.drowned.setAggressive(true);
        this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        this.drowned.stopUsingItem();
        this.drowned.setAggressive(false);
    }

    /**
     * 覆盖溺尸进行远程攻击的手段，具体实现在DrownedAddition，实现这个方法不是Goal的职责
     */
    @Override
    public void performRangedAttack(LivingEntity target) {
        DrownedAddition.performRangedAttack(this.drowned, target, this.drowned.getMainHandItem());
    }

}
