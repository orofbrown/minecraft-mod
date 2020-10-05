package com.youngguns.hybridmobs.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import java.util.UUID;
import java.util.function.Predicate;
import com.youngguns.hybridmobs.entity.ai.goal.ZomeletonAttackGoal;
import com.youngguns.hybridmobs.util.HybridSoundEvents;

public class ZomeletonEntity extends MonsterEntity {
    public static final String ENTITY_NAME = "zomeleton";
    private static final Predicate<Difficulty> HARD_DIFFICULTY_PREDICATE = (p_213697_0_) -> p_213697_0_ == Difficulty.HARD;
    private final BreakDoorGoal breakDoor = new BreakDoorGoal(this, HARD_DIFFICULTY_PREDICATE);
    private boolean isBreakDoorsTaskSet;
    private ResourceLocation deathLootTable = new ResourceLocation("hybridmobs", "entities/zomeleton.json");
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("5da01422-c16e-11ea-a704-acde48001122");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(ZomeletonEntity.class, DataSerializers.BOOLEAN);
    private long deathLootTableSeed;
//    private boolean persistenceRequired;
//    private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
//    protected final float[] inventoryHandsDropChances = new float[2];
//    private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
//    protected final float[] inventoryArmorDropChances = new float[4];

    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }

    public ZomeletonEntity(EntityType<? extends ZomeletonEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }

    @Override
    public void registerGoals() {
        // TODO: Zombie also has goal to attack turtle eggs
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    /*** registerAttributes ***/
    // aka MobEntity.func_233666_p_
    // AttributeModifierMap.func_233814_a_ registers an attr only
    // AttributeModifierMap.func_233815_a_ registers an attr with a value as second arg
    // Attributes are listed with names in net.minecraft.entity.ai.attributes.Attributes.java
    private static AttributeModifierMap.MutableAttribute mobEntityAttrs() {
        return LivingEntity
                .func_233639_cI_()
                .func_233814_a_(Attributes.field_233824_g_);  // attack_knockback
    }

    // aka ZombieEntity.func_234342_eQ_
    private static AttributeModifierMap.MutableAttribute zombieEntityAttrs() {
        return ZomeletonEntity
                .mobEntityAttrs()
                .func_233815_a_(Attributes.field_233819_b_, 35.0D)  // follow_range
                .func_233815_a_(Attributes.field_233821_d_, (double)0.23F)      // movement_speed
                .func_233815_a_(Attributes.field_233823_f_, 3.0D)   // attack_damage
                .func_233815_a_(Attributes.field_233826_i_, 2.0D)   // armor
                .func_233814_a_(Attributes.field_233829_l_);                    // spawn_reinforcements
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return ZomeletonEntity.zombieEntityAttrs();
    }
    /*** end registerAttributes ***/

    @Override
    public void registerData() {
        super.registerData();
        this.getDataManager().register(IS_CHILD, false);
    }


    @Override
    public PathNavigator createNavigator(World worldIn) {
        return super.createNavigator(worldIn);
    }

    @Override
    public float updateDistance(float renderYawOffset, float distance) {
        return super.updateDistance(renderYawOffset, distance);
    }

    protected void func_230291_eT_() {
        this.getAttribute(Attributes.field_233829_l_).setBaseValue(this.rand.nextDouble() * net.minecraftforge.common.ForgeConfig.SERVER.zombieBaseSummonChance.get());
    }

    @Override
    public void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
    }

    public ItemStack getSkullDrop() {
        return new ItemStack(Items.ZOMBIE_HEAD);
    }

    protected void applyAttributeBonuses(float difficulty) {
        this.func_230291_eT_();
        this.getAttribute(Attributes.field_233820_c_).func_233769_c_(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADDITION));
        double d0 = this.rand.nextDouble() * 1.5D * (double)difficulty;
        if (d0 > 1.0D) {
            this.getAttribute(Attributes.field_233819_b_).func_233769_c_(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        if (this.rand.nextFloat() < difficulty * 0.05F) {
            this.getAttribute(Attributes.field_233829_l_).func_233769_c_(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.field_233818_a_).func_233769_c_(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            this.setBreakDoorsAItask(this.canBreakDoors());
        }
    }

    public void setBreakDoorsAItask(boolean enabled) {
        if (this.canBreakDoors()) {
            if (this.isBreakDoorsTaskSet != enabled) {
                this.isBreakDoorsTaskSet = enabled;
                ((GroundPathNavigator)this.getNavigator()).setBreakDoors(enabled);
                if (enabled) {
                    this.goalSelector.addGoal(1, this.breakDoor);
                } else {
                    this.goalSelector.removeGoal(this.breakDoor);
                }
            }
        } else if (this.isBreakDoorsTaskSet) {
            this.goalSelector.removeGoal(this.breakDoor);
            this.isBreakDoorsTaskSet = false;
        }

    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return super.getStandingEyeHeight(poseIn, sizeIn);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return HybridSoundEvents.ENTITY_ZOMELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HybridSoundEvents.ENTITY_ZOMELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected boolean canDropLoot() { return true; }

    protected void dropLoot(DamageSource damageSourceIn, boolean p_213354_2_) {
        super.dropLoot(damageSourceIn, p_213354_2_);
        this.deathLootTable = null;
    }

    protected LootContext.Builder getLootContextBuilder(boolean p_213363_1_, DamageSource damageSourceIn) {
        return super.getLootContextBuilder(p_213363_1_, damageSourceIn).withSeededRandom(this.deathLootTableSeed, this.rand);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("DeathLootTable", 8)) {
            LOGGER.info("HERE @ readAdditional: {}\n{}", compound.getString("DeathLootTable"), compound.getLong("DeathLootTableSeed"));
            this.deathLootTable = new ResourceLocation(compound.getString("DeathLootTable"));
            this.deathLootTableSeed = compound.getLong("DeathLootTableSeed");
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("LeftHanded", this.isLeftHanded());
        if (this.deathLootTable != null) {
            compound.putString("DeathLootTable", this.deathLootTable.toString());
            if (this.deathLootTableSeed != 0L) {
                compound.putLong("DeathLootTableSeed", this.deathLootTableSeed);
            }
        }
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
    }

    @Override
    public void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
    }

    public boolean isChild() {
        return this.getDataManager().get(IS_CHILD);
    }

    public void setChild(boolean childZombie) {
        this.getDataManager().set(IS_CHILD, childZombie);
        if (this.world != null && !this.world.isRemote) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.field_233821_d_);
            modifiableattributeinstance.removeModifier(BABY_SPEED_BOOST);
            if (childZombie) {
                modifiableattributeinstance.func_233767_b_(BABY_SPEED_BOOST);
            }
        }

    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        if (IS_CHILD.equals(key)) {
            this.recalculateSize();
        }

        super.notifyDataManagerChange(key);
    }

//    private void startDrowning(int p_204704_1_) {
//        this.drownedConversionTime = p_204704_1_;
//        this.getDataManager().set(DROWNING, true);
//    }

//    protected void onDrowned() {
//        this.func_234341_c_(EntityType.DROWNED);
//        if (!this.isSilent()) {
//            this.world.playEvent((PlayerEntity)null, 1040, this.func_233580_cy_(), 0);
//        }
//    }

    // Death??
    protected void func_234341_c_(EntityType<? extends ZomeletonEntity> p_234341_1_) {
        ZomeletonEntity entity = this.func_233656_b_(p_234341_1_);
        if (entity != null) {
            entity.applyAttributeBonuses(entity.world.getDifficultyForLocation(entity.func_233580_cy_()).getClampedAdditionalDifficulty());
            entity.setBreakDoorsAItask(entity.canBreakDoors() && this.isBreakDoorsTaskSet());
        }

    }

    protected boolean shouldBurnInDay() {
        return false;
    }

    protected boolean canBreakDoors() {
        return true;
    }

    public int getExperiencePoints(PlayerEntity player) {
        return super.getExperiencePoints(player);
    }

    public boolean shouldDrown() {
        return true;
    }

    public void livingTick() {
        if (this.isAlive()) {
            boolean flag = this.shouldBurnInDay() && this.isInDaylight();
            if (flag) {
                ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageable()) {
                        itemstack.setDamage(itemstack.getDamage() + this.rand.nextInt(2));
                        if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
                            this.sendBreakAnimation(EquipmentSlotType.HEAD);
                            this.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setFire(8);
                }
            }
        }

        super.livingTick();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new ZomeletonAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::isBreakDoorsTaskSet));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.TARGET_DRY_BABY));
    }


//    @Override
//    public CreatureAttribute getCreatureAttribute() {
//        return super.getCreatureAttribute();
//    }

//    @Override
//    public int getMajxFallHeight() {
//        return super.getMaxFallHeight();
//    }

//    @Override
//    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
//        return super.applyPlayerInteraction(player, vec, hand);
//    }

//    @Override
//    public boolean attackEntityAsMob(Entity entityIn) {
//        return super.attackEntityAsMob(entityIn);
//    }

//    @Override
//    public int getMaxSpawnedInChunk() {
//        return super.getMaxSpawnedInChunk();
//    }

//    @Override
//    public void tick() {
//        super.tick();
//    }

//    @Override
//    public boolean attackEntityFrom(DamageSource source, float amount) {
//        super.attackEntityFrom();
//    }

//    @Override
//    public void handleStatusUpdate(byte id) {
//        super.handleStatusUpdate(id);
//    }
}
