package com.youngguns.hybridmobs.client.renderer.entity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.youngguns.hybridmobs.entity.ZomeletonEntity;

import org.apache.logging.log4j.LogManager;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ZomeletonModel extends BipedModel<ZomeletonEntity> {
    public ZomeletonModel(float modelSize, boolean isChild) {
        this(RenderType::getEntityCutoutNoCull, modelSize, 0.0F, 64, isChild ? 32 : 64);
    }

    protected ZomeletonModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        this(RenderType::getEntityCutoutNoCull, modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    public ZomeletonModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(renderTypeIn, modelSizeIn, yOffsetIn, textureWidthIn, textureHeightIn);

        this.bipedLeftArm = new ModelRenderer(this, 24, 40);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSizeIn);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

        this.bipedLeftLeg = new ModelRenderer(this, 16, 40);
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSizeIn);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
    }

    public boolean isAgressive(ZomeletonEntity entityIn) {
        return entityIn.isAggressive();
    }
}