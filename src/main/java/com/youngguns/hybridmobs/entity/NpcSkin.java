package com.youngguns.hybridmobs.entity;

import com.google.common.collect.ImmutableSet;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.village.PointOfInterestType;

public class NpcSkin extends net.minecraftforge.registries.ForgeRegistryEntry<NpcSkin> {
    public static final NpcSkin NONE = null;  // register("none", PointOfInterestType.UNEMPLOYED, (SoundEvent)null);
    public static final NpcSkin ARMORER = null;  // register("armorer", PointOfInterestType.ARMORER, SoundEvents.ENTITY_VILLAGER_WORK_ARMORER);
    public static final NpcSkin BUTCHER = null;  // register("butcher", PointOfInterestType.BUTCHER, SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER);
    public static final NpcSkin CARTOGRAPHER = null;  // register("cartographer", PointOfInterestType.CARTOGRAPHER, SoundEvents.ENTITY_VILLAGER_WORK_CARTOGRAPHER);
    public static final NpcSkin CLERIC = null;  // register("cleric", PointOfInterestType.CLERIC, SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);
    public static final NpcSkin FARMER = null;  // register("farmer", PointOfInterestType.FARMER, ImmutableSet.of(Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.BONE_MEAL), ImmutableSet.of(Blocks.FARMLAND), SoundEvents.ENTITY_VILLAGER_WORK_FARMER);
    public static final NpcSkin FISHERMAN = null;  // register("fisherman", PointOfInterestType.FISHERMAN, SoundEvents.ENTITY_VILLAGER_WORK_FISHERMAN);
    public static final NpcSkin FLETCHER = null;  // register("fletcher", PointOfInterestType.FLETCHER, SoundEvents.ENTITY_VILLAGER_WORK_FLETCHER);
    public static final NpcSkin LEATHERWORKER = null;  // register("leatherworker", PointOfInterestType.LEATHERWORKER, SoundEvents.ENTITY_VILLAGER_WORK_LEATHERWORKER);
    public static final NpcSkin LIBRARIAN = null;  // register("librarian", PointOfInterestType.LIBRARIAN, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);
    public static final NpcSkin MASON = null;  // register("mason", PointOfInterestType.MASON, SoundEvents.ENTITY_VILLAGER_WORK_MASON);
    public static final NpcSkin NITWIT = null;  // register("nitwit", PointOfInterestType.NITWIT, (SoundEvent)null);
    public static final NpcSkin SHEPHERD = null;  // register("shepherd", PointOfInterestType.SHEPHERD, SoundEvents.ENTITY_VILLAGER_WORK_SHEPHERD);
    public static final NpcSkin TOOLSMITH = null;  // register("toolsmith", PointOfInterestType.TOOLSMITH, SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH);
    public static final NpcSkin WEAPONSMITH = null;  // register("weaponsmith", PointOfInterestType.WEAPONSMITH, SoundEvents.ENTITY_VILLAGER_WORK_WEAPONSMITH);
    private final String name;
    private final PointOfInterestType pointOfInterest;
    /** Defines items villagers of this profession can pick up and use. */
    private final ImmutableSet<Item> specificItems;
    /** World blocks this profession interracts with. */
    private final ImmutableSet<Block> relatedWorldBlocks;
    @Nullable
    private final SoundEvent sound;

    public NpcSkin(String nameIn, PointOfInterestType pointOfInterestIn, ImmutableSet<Item> specificItemsIn, ImmutableSet<Block> relatedWorldBlocksIn, @Nullable SoundEvent soundIn) {
        this.name = nameIn;
        this.pointOfInterest = pointOfInterestIn;
        this.specificItems = specificItemsIn;
        this.relatedWorldBlocks = relatedWorldBlocksIn;
        this.sound = soundIn;
    }

    public PointOfInterestType getPointOfInterest() {
        return this.pointOfInterest;
    }

    /**
     * @return A shared static immutable set of the specific items this profession can handle.
     */
    public ImmutableSet<Item> getSpecificItems() {
        return this.specificItems;
    }

    /**
     * @return A shared static immutable set of the world blocks this profession interracts with beside job site block.
     */
    public ImmutableSet<Block> getRelatedWorldBlocks() {
        return this.relatedWorldBlocks;
    }

    @Nullable
    public SoundEvent getSound() {
        return this.sound;
    }

    public String toString() {
        return this.name;
    }

//    static NpcSkin register(String nameIn, PointOfInterestType pointOfInterestIn, @Nullable SoundEvent soundIn) {
//        return register(nameIn, pointOfInterestIn, ImmutableSet.of(), ImmutableSet.of(), soundIn);
//    }
//
//    static NpcSkin register(String nameIn, PointOfInterestType pointOfInterestIn, ImmutableSet<Item> specificItemsIn, ImmutableSet<Block> relatedWorldBlocksIn, @Nullable SoundEvent soundIn) {
//        return Registry.register(Registry.VILLAGER_PROFESSION, new ResourceLocation(nameIn), new NpcSkin(nameIn, pointOfInterestIn, specificItemsIn, relatedWorldBlocksIn, soundIn));
//    }
}