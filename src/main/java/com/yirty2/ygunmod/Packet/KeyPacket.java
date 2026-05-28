package com.yirty2.ygunmod.Packet;


import com.yirty2.ygunmod.Enchants.ModEnchants;
import com.yirty2.ygunmod.Entities.BulletEntity;
import com.yirty2.ygunmod.Items.GunItem;
import com.yirty2.ygunmod.utility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.yirty2.ygunmod.Items.GunItem.*;


public class KeyPacket {
    private String eventName;
        public static void encode(KeyPacket msg, FriendlyByteBuf buf) {buf.writeUtf(msg.eventName,32767);}

        public KeyPacket(String str){this.eventName=str;}


        public static KeyPacket decode(FriendlyByteBuf buf) {
            return new KeyPacket(buf.readUtf(32767));
        }

        public static void handle(KeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;
                ItemStack stack = player.getMainHandItem();
                if(stack.getItem() instanceof GunItem){
                    var gun=getGunClass(stack);
                    if("musket_smoke".equals(msg.eventName)){
                        utility.spawnMusketSmoke(player);
                    }
                    if("force_reload".equals(msg.eventName)){
                        if (isBusy(stack)) {
                            return;
                        }
                        doReload(player,stack);
                    }
                    if("bayonet_switch".equals(msg.eventName)){
                        if (isBusy(stack)){
                            return;
                        }
                        doBayonetSwitch(stack);
                    }
                    if("interact".equals(msg.eventName)){
                        if (isBusy(stack)){
                            return;
                        }
                        int ammo = getAmmo(stack);
                            if (ammo <= 0) {
                                doReload(player,stack);
                                return;
                            }
                            if(isBoltActionType(stack) && isNeedBoltAction(stack)){
                                doBolt(stack);
                                return;
                            }
                            doFire(stack);
                        ServerLevel serverLevel=player.serverLevel();
                        BulletEntity bullet = new BulletEntity(serverLevel, player);
                        bullet.shoot(
                                player.getLookAngle().x,
                                player.getLookAngle().y,
                                player.getLookAngle().z,
                                4f,
                                gun.accuracy
                        );
                        double firepower = EnchantmentHelper.getTagEnchantmentLevel(ModEnchants.FIREPOWER.get(), stack);
                        double dmg=getGunClass(stack).baseDamage;
                        bullet.setBaseDamage(dmg*(1+firepower/4));
                        //加速度で4倍ほどになる
                        //bullet.setNoGravity(true);
                            serverLevel.addFreshEntity(bullet);
                            setAmmo(stack, ammo - 1);
                            setBoltAction(stack, true);
                    }
                    if("ADS_off".equals(msg.eventName)){
                        if(!isBayonet(stack)){
                            posanipush(stack, "sightoff", 5);
                        }
                    }
                    if("ADS_on".equals(msg.eventName)){
                        if(isBayonet(stack)){
                            if(!isBusy(stack)) {
                                doAttack(player, stack);
                            }
                        }else{
                            posanipush(stack, "sighton", 5);
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
}