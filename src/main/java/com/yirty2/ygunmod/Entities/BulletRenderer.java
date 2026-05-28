package com.yirty2.ygunmod.Entities;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulletRenderer extends GeoEntityRenderer<BulletEntity> {
    public BulletRenderer(EntityRendererProvider.Context context) {
        // 先ほど作った BulletModel を渡す
        super(context, new BulletModel());
    }
}