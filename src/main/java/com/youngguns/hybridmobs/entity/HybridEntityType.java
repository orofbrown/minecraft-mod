package com.youngguns.hybridmobs.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ObjectHolder;
import com.youngguns.hybridmobs.HybridMobs;

@ObjectHolder(HybridMobs.MOD_ID)
public class HybridEntityType<T extends Entity> extends EntityType<T> {
    public static final EntityType<NpcEntity> NPC = register(NpcEntity.ENTITY_NAME, EntityType.Builder.create(NpcEntity::new, EntityClassification.MISC).size(0.6F, 1.95F));
    public static final EntityType<ZomeletonEntity> ZOMELETON = register(ZomeletonEntity.ENTITY_NAME, EntityType.Builder.create(ZomeletonEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F));
    public HybridEntityType(
        EntityType.IFactory<T> factory,
        EntityClassification classification,
        boolean field_be,
        boolean serializable,
        boolean summonable,
        boolean immuneToFire,
        ImmutableSet<Block> field_bg,
        EntitySize size,
        int field_bl,
        int field_bm
    ) {
        super(factory, classification, field_be, serializable, summonable, immuneToFire,
                field_bg, size, field_bl, field_bm);
    }

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        // TODO: use ForgeRegistries
        return Registry.register(Registry.ENTITY_TYPE, String.format("hybridmobs:%s", key), builder.build(key));
    }
}