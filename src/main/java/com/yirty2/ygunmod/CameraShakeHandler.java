package com.yirty2.ygunmod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CameraShakeHandler {
    public static int shakeTicks = 0;
    public static float strength = 0;
    public static void shake(int ticks, float amount) {
        shakeTicks = ticks;
        strength = amount;
    }
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        if (shakeTicks > 0)
            shakeTicks--;
    }
    @SubscribeEvent
    public static void camera(ViewportEvent.ComputeCameraAngles event) {
        if (shakeTicks <= 0)
            return;
        float t =(float)(event.getPartialTick() + shakeTicks) * 0.7f;
        float amp =
                strength * ((float)shakeTicks / 20f);
        event.setPitch(
                event.getPitch()
                        + (float)Math.sin(t*8)*amp
        );
        event.setYaw(
                event.getYaw()
                        + (float)Math.cos(t*11)*amp*0.6f
        );
    }
}