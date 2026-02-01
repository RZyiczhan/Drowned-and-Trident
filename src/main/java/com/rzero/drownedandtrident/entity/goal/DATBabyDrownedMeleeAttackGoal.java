package com.rzero.drownedandtrident.entity.goal;

import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Drowned;

/**
 * 可以让溺尸在白天非水中也发动近战攻击的选择Goal，覆盖掉DrownedAttackGoal，主要是给小溺尸使用
 */
public class DATBabyDrownedMeleeAttackGoal extends ZombieAttackGoal {

    private final Drowned drowned;

    public DATBabyDrownedMeleeAttackGoal(Drowned drowned, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(drowned, speedModifier, followingTargetEvenIfNotSeen);
        this.drowned = drowned;
    }

    // 变更处：删除了对是否白天||玩家是否在水中的判断
    @Override
    public boolean canUse() {
        return super.canUse() && drowned.getTarget() != null;
    }

    // 变更处：删除了对是否白天||玩家是否在水中的判断
    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && drowned.getTarget() != null;
    }
}
