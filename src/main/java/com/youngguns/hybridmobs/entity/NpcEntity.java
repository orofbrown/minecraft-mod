package com.youngguns.hybridmobs.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.VillagerTasks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.IReputationTracking;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.GossipManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class NpcEntity extends VillagerEntity {
    public static final String ENTITY_NAME = "npc";

    private static final DataParameter<VillagerData> NPC_DATA = EntityDataManager.createKey(NpcEntity.class, DataSerializers.VILLAGER_DATA);
    /** Mapping between valid food items and their respective efficiency values. */
    public static final Map<Item, Integer> FOOD_VALUES = ImmutableMap.of(Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    /** Defaults items a villager regardless of its profession can pick up. */
    private int timeUntilReset;
    @Nullable
    private PlayerEntity previousCustomer;
    private byte foodLevel;
    private final GossipManager gossip = new GossipManager();
    private long lastGossipDecay;
    private int xp;
    private int field_223725_bO;
    private boolean field_234542_bL_;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.field_234101_d_, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.field_234102_l_, MemoryModuleType.field_234076_J_, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.INTERACTABLE_DOORS, MemoryModuleType.field_225462_q, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.field_226332_A_, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_LAST_SEEN_TIME);
    private static final ImmutableList<SensorType<? extends Sensor<? super NpcEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.field_234129_b_, SensorType.INTERACTABLE_DOORS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_LAST_SEEN);
    private static final Set<Item> ALLOWED_INVENTORY_ITEMS = ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    private long lastRestock;
    private long field_213783_bN;
    private long field_223726_bP;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<NpcEntity, PointOfInterestType>> field_213774_bB = ImmutableMap.of(MemoryModuleType.HOME, (p_213769_0_, p_213769_1_) -> p_213769_1_ == PointOfInterestType.HOME, MemoryModuleType.JOB_SITE, (p_213771_0_, p_213771_1_) -> p_213771_0_.getVillagerData().getProfession().getPointOfInterest() == p_213771_1_, MemoryModuleType.field_234101_d_, (p_213772_0_, p_213772_1_) -> PointOfInterestType.ANY_VILLAGER_WORKSTATION.test(p_213772_1_), MemoryModuleType.MEETING_POINT, (p_234546_0_, p_234546_1_) -> p_234546_1_ == PointOfInterestType.MEETING);

    public NpcEntity(EntityType<? extends NpcEntity> type, World worldIn) {
        this(type, worldIn, INpcType.NPC_ONE);
    }

    private NpcEntity(EntityType<? extends NpcEntity> type, World worldIn, INpcType npcType) {
        super(type, worldIn, npcType);
    }

    protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
        Brain<VillagerEntity> brain = this.func_230289_cH_().func_233748_a_(dynamicIn);
        this.initBrain(brain);
        return brain;
    }

    protected void onGrowingAdult() {
        super.onGrowingAdult();
        if (this.world instanceof ServerWorld) {
            this.resetBrain((ServerWorld)this.world);
        }

    }

    protected void updateAITasks() {
        this.world.getProfiler().startSection("villagerBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().endSection();
        if (this.field_234542_bL_) {
            this.field_234542_bL_ = false;
        }

        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }

        if (this.previousCustomer != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).updateReputation(IReputationType.TRADE, this.previousCustomer, this);
            this.world.setEntityState(this, (byte)14);
            this.previousCustomer = null;
        }

        if (!this.isAIDisabled() && this.rand.nextInt(100) == 0) {
            Raid raid = ((ServerWorld)this.world).findRaid(this.func_233580_cy_());
            if (raid != null && raid.isActive() && !raid.isOver()) {
                this.world.setEntityState(this, (byte)42);
            }
        }

        if (this.getVillagerData().getProfession() == VillagerProfession.NONE && this.hasCustomer()) {
            this.resetCustomer();
        }

        super.updateAITasks();
    }

    protected void resetCustomer() {
        super.resetCustomer();
        this.resetAllSpecialPrices();
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(NPC_DATA, new VillagerData(INpcType.NPC_ONE, VillagerProfession.NONE, 1));
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        } else {
            return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    protected void onVillagerTrade(MerchantOffer offer) {
        int i = 3 + this.rand.nextInt(4);
        this.xp += offer.getGivenExp();
        this.previousCustomer = this.getCustomer();
        if (offer.getDoesRewardExp()) {
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), i));
        }

    }

    protected ITextComponent getProfessionName() {
        net.minecraft.util.ResourceLocation profName = this.getVillagerData().getProfession().getRegistryName();
        return new TranslationTextComponent(this.getType().getTranslationKey() + '.' + (!"minecraft".equals(profName.getNamespace()) ? profName.getNamespace() + '.' : "") + profName.getPath());
    }

    protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.func_230293_i_(itemstack)) {
            Inventory inventory = this.getVillagerInventory();
            boolean flag = inventory.func_233541_b_(itemstack);
            if (!flag) {
                return;
            }

            this.func_233630_a_(itemEntity);
            this.onItemPickup(itemEntity, itemstack.getCount());
            ItemStack itemstack1 = inventory.addItem(itemstack);
            if (itemstack1.isEmpty()) {
                itemEntity.remove();
            } else {
                itemstack.setCount(itemstack1.getCount());
            }
        }

    }

//    protected void populateTradeData() {
//        super.populateTradeData();
//    }

    private void tickGossip() {
        long i = this.world.getGameTime();
        if (this.lastGossipDecay == 0L) {
            this.lastGossipDecay = i;
        } else if (i >= this.lastGossipDecay + 24000L) {
            this.gossip.tick();
            this.lastGossipDecay = i;
        }
    }

    private void updateGolemLastSeenMemory(long gameTime) {
        this.brain.setMemory(MemoryModuleType.GOLEM_LAST_SEEN_TIME, gameTime);
    }

    private boolean hasSeenGolemRecently(long gameTime) {
        Optional<Long> optional = this.brain.getMemory(MemoryModuleType.GOLEM_LAST_SEEN_TIME);
        if (!optional.isPresent()) {
            return false;
        } else {
            Long olong = optional.get();
            return gameTime - olong <= 600L;
        }
    }

    @Nullable
    private IronGolemEntity trySpawnGolem() {
        BlockPos blockpos = this.func_233580_cy_();

        for(int i = 0; i < 10; ++i) {
            double d0 = (double)(this.world.rand.nextInt(16) - 8);
            double d1 = (double)(this.world.rand.nextInt(16) - 8);
            BlockPos blockpos1 = this.func_241433_a_(blockpos, d0, d1);
            if (blockpos1 != null) {
                IronGolemEntity irongolementity = EntityType.IRON_GOLEM.create(this.world, (CompoundNBT)null, (ITextComponent)null, (PlayerEntity)null, blockpos1, SpawnReason.MOB_SUMMONED, false, false);
                if (irongolementity != null) {
                    if (irongolementity.canSpawn(this.world, SpawnReason.MOB_SUMMONED) && irongolementity.isNotColliding(this.world)) {
                        this.world.addEntity(irongolementity);
                        return irongolementity;
                    }

                    irongolementity.remove();
                }
            }
        }

        return null;
    }

    @Nullable
    private BlockPos func_241433_a_(BlockPos p_241433_1_, double p_241433_2_, double p_241433_4_) {
        int i = 6;
        BlockPos blockpos = p_241433_1_.add(p_241433_2_, 6.0D, p_241433_4_);
        BlockState blockstate = this.world.getBlockState(blockpos);

        for(int j = 6; j >= -6; --j) {
            BlockPos blockpos1 = blockpos;
            BlockState blockstate1 = blockstate;
            blockpos = blockpos.down();
            blockstate = this.world.getBlockState(blockpos);
            if ((blockstate1.isAir() || blockstate1.getMaterial().isLiquid()) && blockstate.getMaterial().isOpaque()) {
                return blockpos1;
            }
        }

        return null;
    }

    private void func_223718_eH() {
        this.func_223719_ex();
        this.field_223725_bO = 0;
    }

    private boolean hasSleptAndWorkedRecently(long gameTime) {
        Optional<Long> optional = this.brain.getMemory(MemoryModuleType.LAST_SLEPT);
        if (optional.isPresent()) {
            return gameTime - optional.get() < 24000L;
        } else {
            return false;
        }
    }

    private void shakeHead() {
        this.setShakeHeadTicks(40);
        if (!this.world.isRemote()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    private void displayMerchantGui(PlayerEntity player) {
        this.recalculateSpecialPricesFor(player);
        this.setCustomer(player);
        this.openMerchantContainer(player, this.getDisplayName(), this.getVillagerData().getLevel());
    }

    private void resetAllSpecialPrices() {
        for(MerchantOffer merchantoffer : this.getOffers()) {
            merchantoffer.resetSpecialPrice();
        }

    }

    private boolean hasUsedOffer() {
        for(MerchantOffer merchantoffer : this.getOffers()) {
            if (merchantoffer.hasBeenUsed()) {
                return true;
            }
        }

        return false;
    }

    private boolean func_223720_ew() {
        return this.field_223725_bO == 0 || this.field_223725_bO < 2 && this.world.getGameTime() > this.lastRestock + 2400L;
    }

    private void func_223719_ex() {
        int i = 2 - this.field_223725_bO;
        if (i > 0) {
            for(MerchantOffer merchantoffer : this.getOffers()) {
                merchantoffer.resetUses();
            }
        }

        for(int j = 0; j < i; ++j) {
            this.calculateDemandOfOffers();
        }

    }

    private void calculateDemandOfOffers() {
        for(MerchantOffer merchantoffer : this.getOffers()) {
            merchantoffer.calculateDemand();
        }

    }

    private void recalculateSpecialPricesFor(PlayerEntity playerIn) {
        int i = this.getPlayerReputation(playerIn);
        if (i != 0) {
            for(MerchantOffer merchantoffer : this.getOffers()) {
                merchantoffer.increaseSpecialPrice(-MathHelper.floor((float)i * merchantoffer.getPriceMultiplier()));
            }
        }

        if (playerIn.isPotionActive(Effects.HERO_OF_THE_VILLAGE)) {
            EffectInstance effectinstance = playerIn.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE);
            int k = effectinstance.getAmplifier();

            for(MerchantOffer merchantoffer1 : this.getOffers()) {
                double d0 = 0.3D + 0.0625D * (double)k;
                int j = (int)Math.floor(d0 * (double)merchantoffer1.getBuyingStackFirst().getCount());
                merchantoffer1.increaseSpecialPrice(-Math.max(j, 1));
            }
        }

    }

    private void func_223361_a(Entity p_223361_1_) {
        if (this.world instanceof ServerWorld) {
            Optional<List<LivingEntity>> optional = this.brain.getMemory(MemoryModuleType.VISIBLE_MOBS);
            if (optional.isPresent()) {
                ServerWorld serverworld = (ServerWorld)this.world;
                optional.get().stream().filter((p_223349_0_) -> {
                    return p_223349_0_ instanceof IReputationTracking;
                }).forEach((p_223342_2_) -> {
                    serverworld.updateReputation(IReputationType.VILLAGER_KILLED, p_223361_1_, (IReputationTracking)p_223342_2_);
                });
            }
        }
    }

    private boolean func_223344_ex() {
        return this.foodLevel < 12;
    }

    private int getFoodValueFromInventory() {
        Inventory inventory = this.getVillagerInventory();
        return FOOD_VALUES.entrySet().stream().mapToInt((p_226553_1_) -> {
            return inventory.count(p_226553_1_.getKey()) * p_226553_1_.getValue();
        }).sum();
    }

    private void func_213765_en() {
        if (this.func_223344_ex() && this.getFoodValueFromInventory() != 0) {
            for(int i = 0; i < this.getVillagerInventory().getSizeInventory(); ++i) {
                ItemStack itemstack = this.getVillagerInventory().getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    Integer integer = FOOD_VALUES.get(itemstack.getItem());
                    if (integer != null) {
                        int j = itemstack.getCount();

                        for(int k = j; k > 0; --k) {
                            this.foodLevel = (byte)(this.foodLevel + integer);
                            this.getVillagerInventory().decrStackSize(i, 1);
                            if (!this.func_223344_ex()) {
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    private void decrFoodLevel(int qty) {
        this.foodLevel = (byte)(this.foodLevel - qty);
    }

    private void initBrain(Brain<VillagerEntity> villagerBrain) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        if (this.isChild()) {
            villagerBrain.setSchedule(Schedule.VILLAGER_BABY);
            villagerBrain.registerActivity(Activity.PLAY, VillagerTasks.play(0.5F));
        } else {
            villagerBrain.setSchedule(Schedule.VILLAGER_DEFAULT);
            villagerBrain.func_233700_a_(Activity.WORK, VillagerTasks.work(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_PRESENT)));
        }

        villagerBrain.registerActivity(Activity.CORE, VillagerTasks.core(villagerprofession, 0.5F));
        villagerBrain.func_233700_a_(Activity.MEET, VillagerTasks.meet(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleStatus.VALUE_PRESENT)));
        villagerBrain.registerActivity(Activity.REST, VillagerTasks.rest(villagerprofession, 0.5F));
        villagerBrain.registerActivity(Activity.IDLE, VillagerTasks.idle(villagerprofession, 0.5F));
        villagerBrain.registerActivity(Activity.PANIC, VillagerTasks.panic(villagerprofession, 0.5F));
        villagerBrain.registerActivity(Activity.PRE_RAID, VillagerTasks.preRaid(villagerprofession, 0.5F));
        villagerBrain.registerActivity(Activity.RAID, VillagerTasks.raid(villagerprofession, 0.5F));
        villagerBrain.registerActivity(Activity.HIDE, VillagerTasks.hide(villagerprofession, 0.5F));
        villagerBrain.setDefaultActivities(ImmutableSet.of(Activity.CORE));
        villagerBrain.setFallbackActivity(Activity.IDLE);
        villagerBrain.switchTo(Activity.IDLE);
        villagerBrain.updateActivity(this.world.getDayTime(), this.world.getGameTime());
    }

    private boolean canLevelUp() {
        return false;
    }
}
