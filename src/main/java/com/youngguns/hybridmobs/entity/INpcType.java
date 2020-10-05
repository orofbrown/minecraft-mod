package com.youngguns.hybridmobs.entity;

import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface INpcType extends IVillagerType {
    INpcType NPC_ONE = register("npc_1");
    INpcType NPC_TWO= register("npc_2");
    INpcType NPC_THREE = register("npc_3");
    INpcType NPC_FOUR = register("npc_4");
    INpcType NPC_FIVE = register("npc_5");
    INpcType NPC_SIX = register("npc_6");
    INpcType NPC_SEVEN = register("npc_7");
    INpcType NPC_EIGHT = register("npc_8");
    INpcType NPC_NINE = register("npc_9");

    static INpcType register(final String key) {
        return Registry.register(Registry.VILLAGER_TYPE, new ResourceLocation(key), new INpcType() {
            public String toString() {
                return key;
            }
        });
    }

    static INpcType byBiome(Biome biomeIn) {
        return INpcType.NPC_ONE;
    }
}
