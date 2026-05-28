package com.yirty2.ygunmod.Items;

import com.yirty2.ygunmod.Items.GunItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GunModel extends GeoModel<GunItem> {
    @Override
    public ResourceLocation getModelResource(GunItem animatable) {
        return ResourceLocation.parse(
                "ygunmod:geo/"+animatable.name+".json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(GunItem animatable) {
        return ResourceLocation.parse(
                "ygunmod:textures/item/"+animatable.name+".png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(GunItem animatable) {
        return ResourceLocation.parse(
                "ygunmod:animations/"+animatable.name+".json"
        );
    }
}