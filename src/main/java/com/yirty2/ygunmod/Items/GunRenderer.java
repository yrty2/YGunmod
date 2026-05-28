package com.yirty2.ygunmod.Items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static com.yirty2.ygunmod.Items.GunItem.isADS;

public class GunRenderer extends GeoItemRenderer<GunItem> {

    public GunRenderer(GunModel gun) {
        super(gun);
    }
    @Override
    public void renderRecursively(
            PoseStack poseStack,
            GunItem animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        if (bone.getName().equals("Larm")
                || bone.getName().equals("Rarm")) {

            Minecraft mc = Minecraft.getInstance();

            if (mc.player != null) {

                ResourceLocation skin =
                        mc.player.getSkinTextureLocation();

                RenderType armRender =
                        RenderType.entitySolid(skin);

                VertexConsumer armBuffer =
                        bufferSource.getBuffer(armRender);

                super.renderRecursively(
                        poseStack,
                        animatable,
                        bone,
                        armRender,
                        bufferSource,
                        armBuffer,
                        isReRender,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        red,
                        green,
                        blue,
                        alpha
                );

                return;
            }
        }

        VertexConsumer normalBuffer =
                bufferSource.getBuffer(renderType);

        super.renderRecursively(
                poseStack,
                animatable,
                bone,
                renderType,
                bufferSource,
                normalBuffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );
    }
}