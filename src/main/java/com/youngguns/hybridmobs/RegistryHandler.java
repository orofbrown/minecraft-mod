package com.youngguns.hybridmobs;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.youngguns.hybridmobs.entity.HybridEntityType;
import com.youngguns.hybridmobs.entity.NpcEntity;
import com.youngguns.hybridmobs.entity.ZomeletonEntity;

@EventBusSubscriber(modid = HybridMobs.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<EntityType<?>, SpawnEggItem> SPAWN_EGGS = new HashMap<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // Register items
        // event.getRegistry().registerAll();

        // Register mobs
        build(
            HybridEntityType.NPC,
            NpcEntity.ENTITY_NAME,
                0x226e37,
                0x00ffffff
                 // 0x33a352 // 0x466a50
        );
        build(
                HybridEntityType.ZOMELETON,
                ZomeletonEntity.ENTITY_NAME,
                0x00afaf,     // 0xc1c1c1
                0x494949    // 0x799c65
        );

        for (SpawnEggItem spawnEggItem : SPAWN_EGGS.values()) {
            event.getRegistry().register(spawnEggItem);
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        // TODO: use ForgeRegistries
        GlobalEntityTypeAttributes.put(HybridEntityType.NPC, ZomeletonEntity.registerAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(HybridEntityType.ZOMELETON, ZomeletonEntity.registerAttributes().func_233813_a_());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        // Zomeleton
        event.getRegistry().register(createSoundEvent("entity.zomeleton.hurt"));
        event.getRegistry().register(createSoundEvent("entity.zomeleton.death"));
    }

    private static SoundEvent createSoundEvent(String name) {
        return setRegistryName(name, new SoundEvent(HybridMobs.prefix(name)));
    }

    private static <T extends MobEntity> EntityType<T> build(EntityType<T> entityType, String name, int eggPrimaryColor, int eggSecondaryColor) {
        SpawnEggItem item = new SpawnEggItem(entityType, eggPrimaryColor, eggSecondaryColor, defaultProperty());
        setRegistryName(String.format("%s_spawn_egg", name), item);
        SPAWN_EGGS.put(entityType, item);

        return entityType;
    }

    public static void registerDispenseBehavior() {
        DefaultDispenseItemBehavior defaultdispenseitembehavior = new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().get(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                entityType.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };

        for (SpawnEggItem spawnEggItem : SPAWN_EGGS.values()) {
            DispenserBlock.registerDispenseBehavior(spawnEggItem, defaultdispenseitembehavior);
        }

        SPAWN_EGGS.clear();
    }

    private static Item.Properties defaultProperty() {
        return new Item.Properties().group(HybridMobs.ITEM_GROUP);
    }

    private static <T extends IForgeRegistryEntry<?>> T setRegistryName(String name, T entry) {
        entry.setRegistryName(HybridMobs.prefix(name));
        return entry;
    }

//    @SubscribeEvent
//    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) { }

//    @SubscribeEvent
//    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) { }

//    @SubscribeEvent
//    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) { }

//    @SubscribeEvent
//    public static void remapParticleTypes(RegistryEvent.MissingMappings<ParticleType<?>> event) { }

//    private static SoundEvent createSoundEvent(String name) {
//        return setRegistryName(name, new SoundEvent(MutantBeasts.prefix(name)));
//    }

//    @SubscribeEvent
//    public static void onModConfigEvent(ModConfig.ModConfigEvent event) {
//        MBConfig.bake(event.getConfig().getSpec());
//    }
}
