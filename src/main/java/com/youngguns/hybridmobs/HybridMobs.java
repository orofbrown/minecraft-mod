package com.youngguns.hybridmobs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HybridMobs.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid= HybridMobs.MOD_ID)
public class HybridMobs {
    private static final Logger LOGGER = LogManager.getLogger();

    public final static String MOD_ID = "hybridmobs";
    public final static String NAME = "Hybrid Mobs";
    public final static String VERSION = "1.0";
    public static final String TARGETVERSION = "1.16.1";

    public static final ItemGroup ITEM_GROUP = ItemGroup.MISC;

    public HybridMobs() {
        // MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        net.minecraftforge.fml.DeferredWorkQueue.runLater(() -> {
            RegistryHandler.registerDispenseBehavior();
        });
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) { }

    public static ResourceLocation getEntityTexture(String name) {
        LOGGER.info(String.format("HERE @ getEntityTexture! :: textures/entity/%s.png", name));
        return prefix(String.format("textures/entity/%s.png", name));
    }
}
