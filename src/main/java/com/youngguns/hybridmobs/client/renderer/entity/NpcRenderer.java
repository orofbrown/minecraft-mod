package com.youngguns.hybridmobs.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.VillagerLevelPendantLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.youngguns.hybridmobs.HybridMobs;
import com.youngguns.hybridmobs.client.renderer.entity.model.NpcModel;
import com.youngguns.hybridmobs.entity.NpcEntity;

@OnlyIn(Dist.CLIENT)
public class NpcRenderer <T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<NpcEntity, NpcModel> {
    private static final ResourceLocation TEXTURE = HybridMobs.getEntityTexture(String.format("npc/npc_%d", new Random().nextInt(9) + 1));

    public NpcRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NpcModel(0.0F), 0.5F);

        this.addLayer(new HeadLayer<>(this));
        this.addLayer(
                new VillagerLevelPendantLayer<>(
                        this,
                        (IReloadableResourceManager)Minecraft.getInstance().getResourceManager(),
                        "npc"
                )
        );
        this.addLayer(new CrossedArmsItemLayer<>(this));
    }

    protected void preRenderCallback(NpcEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        if (entitylivingbaseIn.isChild()) {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }

        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getEntityTexture(NpcEntity entity) {
        return TEXTURE;
    }
}