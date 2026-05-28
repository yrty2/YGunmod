package com.yirty2.ygunmod.Events;

import com.yirty2.ygunmod.Items.GunItem;
import com.yirty2.ygunmod.Items.ModItems;
import com.yirty2.ygunmod.ModKeyMappings;
import com.yirty2.ygunmod.Packet.KeyPacket;
import com.yirty2.ygunmod.Packet.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.yirty2.ygunmod.Items.GunItem.isADS;

@Mod.EventBusSubscriber(modid = "ygunmod", value = Dist.CLIENT)
public class ClientInputHandler {
    private static boolean oldADS = false;
    @SubscribeEvent
    public static void onLeftClick(InputEvent.InteractionKeyMappingTriggered event){
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if(!event.isAttack()){
            return;
        }
        ItemStack stack = mc.player.getMainHandItem();
        if (stack.getItem() instanceof GunItem){
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null) return;
        ItemStack stack = mc.player.getMainHandItem();
        boolean holdingGun =stack.getItem() instanceof GunItem;
        if (ModKeyMappings.RELOAD_KEY.consumeClick()){
            if(holdingGun) {
                NetworkHandler.INSTANCE.sendToServer(new KeyPacket("force_reload"));
            }
        }
        if (ModKeyMappings.BAYONET_KEY.consumeClick()){
            if(holdingGun && !isADS(stack)){
                NetworkHandler.INSTANCE.sendToServer(new KeyPacket("bayonet_switch"));
            }
        }
        if(!holdingGun) {
            oldADS = false;
            return;
        }
        //右クリ
        boolean ads = mc.options.keyUse.isDown();
        if(ads && !oldADS) {
            NetworkHandler.INSTANCE.sendToServer(
                    new KeyPacket("ADS_on")
            );
        }
        if(!ads && oldADS) {
            NetworkHandler.INSTANCE.sendToServer(
                    new KeyPacket("ADS_off")
            );
        }
        oldADS = ads;
        //左クリック判定
        boolean leftPressed = mc.options.keyAttack.isDown();
        if(leftPressed){
            NetworkHandler.INSTANCE.sendToServer(new KeyPacket("interact"));
        }
    }
}