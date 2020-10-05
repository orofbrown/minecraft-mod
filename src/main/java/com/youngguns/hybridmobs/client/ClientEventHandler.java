package com.youngguns.hybridmobs.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.youngguns.hybridmobs.HybridMobs;
import com.youngguns.hybridmobs.client.renderer.entity.NpcRenderer;
import com.youngguns.hybridmobs.client.renderer.entity.ZomeletonRenderer;
import com.youngguns.hybridmobs.entity.HybridEntityType;

@EventBusSubscriber(modid = HybridMobs.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(HybridEntityType.NPC, NpcRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HybridEntityType.ZOMELETON, ZomeletonRenderer::new);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) { }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) { }

    @SubscribeEvent
    public static void onParticleFactoryRegistry(ParticleFactoryRegisterEvent event) { }
}
