package com.youngguns.hybridmobs.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import com.youngguns.hybridmobs.HybridMobs;
import com.youngguns.hybridmobs.client.renderer.entity.model.ZomeletonModel;
import com.youngguns.hybridmobs.entity.ZomeletonEntity;

import org.apache.logging.log4j.LogManager;

public class ZomeletonRenderer extends HybridMobRenderer<ZomeletonEntity, ZomeletonModel> {
    private static final ResourceLocation TEXTURE = HybridMobs.getEntityTexture("zomeleton/zomeleton");
//    private static final ResourceLocation TEXTURE = HybridMobs.getEntityTexture("miner_zombie");

    public ZomeletonRenderer(EntityRendererManager manager) {
        super(manager, new ZomeletonModel(0.0F, false), 1.0F);
        this.addLayer(new BipedArmorLayer<>(this, new ZomeletonModel(0.5F, true), new ZomeletonModel(1.0F, true)));
    }

//    TODO: Removed in 1.16?
//    @Override
//    protected void renderModel(ZomeletonEntity living, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
//        if (living.vanishTime > 0) {
//            GlStateManager.enableNormalize();
//            GlStateManager.enableBlend();
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F - ((float)living.vanishTime + this.entityModel.getPartialTick()) / (float)ZomeletonEntity.MAX_DEATH_TIME * 0.6F);
//        }
//
//        super.renderModel(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
//
//        if (living.vanishTime > 0) {
//            GlStateManager.disableBlend();
//            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }

//    TODO: Removed in 1.16?
//    @Override
//    protected void preRenderCallback(ZomeletonEntity entitylivingbaseIn, float partialTickTime) {
//        GlStateManager.scalef(1.3F, 1.3F, 1.3F);
//    }

//    TODO: Removed in 1.16?
//    @Override
//    protected void applyRotations(ZomeletonEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
//        if (entityLiving.deathTime > 0) {
//            GlStateManager.rotatef(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
//            int pitch = Math.min(20, entityLiving.deathTime);
//            boolean reviving = false;
//
//            if (entityLiving.deathTime > ZomeletonEntity.MAX_DEATH_TIME - 40) {
//                pitch = ZomeletonEntity.MAX_DEATH_TIME - entityLiving.deathTime;
//                reviving = true;
//            }
//
//            if (pitch > 0) {
//                float f = ((float)pitch + partialTicks - 1.0F) / 20.0F * 1.6F;
//
//                if (reviving) {
//                    f = ((float)pitch - partialTicks) / 40.0F * 1.6F;
//                }
//
//                f = MathHelper.sqrt(f);
//
//                if (f > 1.0F) {
//                    f = 1.0F;
//                }
//
//                GlStateManager.rotatef(f * this.getDeathMaxRotation(entityLiving), -1.0F, 0.0F, 0.0F);
//            }
//        } else {
//            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
//        }
//    }

    @Override
    protected float getDeathMaxRotation(ZomeletonEntity living) {
        return 90.0F;
    }

    @Override
    public ResourceLocation getEntityTexture(ZomeletonEntity entity) {
        LogManager.getLogger().info("HERE @ Zomeleton.getEntityTexture! {}", entity);
        return TEXTURE;
    }
}