package com.youngguns.hybridmobs.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.Util;

public abstract class HybridMobRenderer<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private static final DynamicTexture TEXTURE_BRIGHTNESS = Util.make(new DynamicTexture(16, 16, false), (dynamicTexture) -> {
        dynamicTexture.getTextureData().untrack();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                dynamicTexture.getTextureData().setPixelRGBA(j, i, -1);
            }
        }

        dynamicTexture.updateDynamicTexture();
    });

    public HybridMobRenderer(EntityRendererManager manager, M model, float shadowSize) {
        super(manager, model, shadowSize);
    }


//    TODO: Removed in 1.16?
//    @Override
//    protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
//        float hurtColor = ((float)entitylivingbaseIn.hurtTime - partialTicks) / (float)entitylivingbaseIn.maxHurtTime;
//        int colorMuliplier = this.getColorMultiplier(entitylivingbaseIn, entitylivingbaseIn.getBrightness(), partialTicks);
//        boolean hasColorMuliplier = (colorMuliplier >> 24 & 255) > 0;
//        boolean wasHurt = entitylivingbaseIn.hurtTime > 0;
//        if (!hasColorMuliplier && !wasHurt) {
//            return false;
//        } else if (!hasColorMuliplier && !combineTextures) {
//            return false;
//        } else {
//            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
//            GlStateManager.enableTexture();
//            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
//            GlStateManager.activeTexture(GLX.GL_TEXTURE1);
//            GlStateManager.enableTexture();
//            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, GLX.GL_INTERPOLATE);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_CONSTANT);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE2_RGB, GLX.GL_CONSTANT);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND2_RGB, 770);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
//            this.brightnessBuffer.position(0);
//            if (wasHurt) {
//                this.brightnessBuffer.put(hurtColor);
//                this.brightnessBuffer.put(0.0F);
//                this.brightnessBuffer.put(0.0F);
//                this.brightnessBuffer.put(0.3F);
//                this.brightnessBuffer.flip();
//            }
//
//            if (hasColorMuliplier) {
//                float alpha = (float)(colorMuliplier >> 24 & 255) / 255.0F;
//                float red = (float)(colorMuliplier >> 16 & 255) / 255.0F;
//                float green = (float)(colorMuliplier >> 8 & 255) / 255.0F;
//                float blue = (float)(colorMuliplier & 255) / 255.0F;
//                this.brightnessBuffer.put(red);
//                this.brightnessBuffer.put(green);
//                this.brightnessBuffer.put(blue);
//                this.brightnessBuffer.put(alpha);
//                this.brightnessBuffer.flip();
//            }
//
//            GlStateManager.texEnv(8960, 8705, this.brightnessBuffer);
//            GlStateManager.activeTexture(GLX.GL_TEXTURE2);
//            GlStateManager.enableTexture();
//            GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
//            GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_PREVIOUS);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_TEXTURE1);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
//            GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
//            GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
//            GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
//            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
//            return true;
//        }
//    }
}