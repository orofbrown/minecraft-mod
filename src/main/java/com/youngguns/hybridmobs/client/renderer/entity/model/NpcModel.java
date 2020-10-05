package com.youngguns.hybridmobs.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.youngguns.hybridmobs.entity.NpcEntity;

@OnlyIn(Dist.CLIENT)
public class NpcModel extends VillagerModel<NpcEntity> {
    public NpcModel(float scale) {
        this(scale, 64, 64);
    }

    public NpcModel(float p_i51059_1_, int p_i51059_2_, int p_i51059_3_) {
        super(p_i51059_1_, p_i51059_2_, p_i51059_3_);
        // Do texture stuff here
    }

//    public Iterable<ModelRenderer> getParts() {
//        return ImmutableList.of(this.villagerHead, this.villagerBody, this.rightVillagerLeg, this.leftVillagerLeg, this.villagerArms);
//    }
//
//    public void setRotationAngles(NpcEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//    }
//
//    public ModelRenderer getModelHead() {
//        return super.getModelHead();
//    }
//
//    public void func_217146_a(boolean p_217146_1_) {
//        super.func_217146_a(p_217146_1_);
//    }
}