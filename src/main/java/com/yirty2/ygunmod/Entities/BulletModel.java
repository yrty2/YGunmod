package com.yirty2.ygunmod.Entities;

import com.yirty2.ygunmod.Entities.BulletEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.yirty2.ygunmod.Ygunmod.MODID;

public class BulletModel extends GeoModel<BulletEntity> {

    @Override
    public ResourceLocation getModelResource(BulletEntity animatable) {
        return ResourceLocation.parse("ygunmod:geo/simple_bullet.json");
    }

    @Override
    public ResourceLocation getTextureResource(BulletEntity animatable) {
        return ResourceLocation.parse("ygunmod:textures/entity/simple_bullet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BulletEntity animatable) {
        return ResourceLocation.parse("ygunmod:animations/simple_bullet.json");
    }
}