package com.youngguns.hybridmobs.entity.ai.goal;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import com.youngguns.hybridmobs.entity.ZomeletonEntity;

/*** Copied from ZombieAttackGoal.java ***/
public class ZomeletonAttackGoal extends MeleeAttackGoal {
    private final ZomeletonEntity zombie;
    private int raiseArmTicks;

    public ZomeletonAttackGoal(ZomeletonEntity zombieIn, double speedIn, boolean longMemoryIn) {
        super(zombieIn, speedIn, longMemoryIn);
        this.zombie = zombieIn;
    }

    public void startExecuting() {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    public void resetTask() {
        super.resetTask();
        this.zombie.setAggroed(false);
    }

    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.func_234041_j_() < this.func_234042_k_() / 2) {
            this.zombie.setAggroed(true);
        } else {
            this.zombie.setAggroed(false);
        }

    }
}