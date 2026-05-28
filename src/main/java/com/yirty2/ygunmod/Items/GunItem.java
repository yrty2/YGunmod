package com.yirty2.ygunmod.Items;

import com.yirty2.ygunmod.CameraShakeHandler;
import com.yirty2.ygunmod.Packet.KeyPacket;
import com.yirty2.ygunmod.Packet.NetworkHandler;
import com.yirty2.ygunmod.utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Consumer;

public class GunItem extends Item implements GeoItem {
    public final int maxAmmo;
    //43,15,10
    public final boolean boltaction;
    public final String name;
    public final String controllername;
    public final String pcontrollername;
    public final int reloadTick;
    public final int boltTick;
    public final double baseDamage;
    public final Item bulletType;
    public final boolean hasBayonet;
    public int ammoinput=0;
    public float accuracy=0;
    public final int fireTick;//also FireRate
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public GunItem(Properties properties,String namestr,int ft,int rt,int ammo,double dam,Item btp,boolean hasbayonet,float acc,int bt){
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        name=namestr;
        controllername=name+"_"+"controller";
        pcontrollername=name+"_"+"pose_controller";
        reloadTick=rt;
        fireTick=ft;
        baseDamage=dam;
        boltaction=bt!=0;
        boltTick=bt;
        hasBayonet=hasbayonet;
        maxAmmo=ammo;
        bulletType=btp;
        accuracy=acc;
    }

    //アニメーションをクライアントが再生するために必須。
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(
            Consumer<IClientItemExtensions> consumer
    ) {
        consumer.accept(new IClientItemExtensions() {
            private GunRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer(){
                if (renderer==null) {
                    renderer=new GunRenderer(new GunModel());
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        var acontroller=new AnimationController<>(this, controllername, 0, state -> {
            ItemStack stack = state.getData(software.bernie.geckolib.constant.DataTickets.ITEMSTACK);
            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }).triggerableAnim(name+"_fire",
                        RawAnimation.begin().thenPlay("fire"))
                .triggerableAnim(name+"_bolt",
                        RawAnimation.begin().thenPlay("bolt"))
                .triggerableAnim(name+"_reload",
                        RawAnimation.begin().thenPlay("Reload"))
                .setSoundKeyframeHandler(event -> {
                            String soundName = event.getKeyframeData().getSound();
                            Minecraft mc = Minecraft.getInstance();

                            if (mc.player == null || mc.level == null)
                                return;

                            ResourceLocation id;

                            if (soundName.contains(":")) {
                                id = ResourceLocation.parse(soundName);
                            } else {
                                id = ResourceLocation.parse("ygunmod:"+soundName);
                            }
                            if (!BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
                                System.out.println("Sound not registered: " + id);
                                return;
                            }
                            SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(id);
                            mc.level.playLocalSound(
                                    mc.player.getX(),
                                    mc.player.getY(),
                                    mc.player.getZ(),
                                    sound,
                                    net.minecraft.sounds.SoundSource.PLAYERS,
                                    1f,
                                    1f,
                                    false
                            );
                        });
        if(hasBayonet) {
            acontroller
                    .triggerableAnim(name+"_bayoneton",
                    RawAnimation.begin().thenPlay("bayoneton"))
                    .triggerableAnim(name+"_bayonetoff",
                    RawAnimation.begin().thenPlay("bayonetoff"))
                    .triggerableAnim(name+"_attack",
                    RawAnimation.begin().thenPlay("attack"));
        }
        controllers.add(acontroller
        );
        controllers.add(new AnimationController<>(this, pcontrollername, 0, state -> {
                    ItemStack stack = state.getData(software.bernie.geckolib.constant.DataTickets.ITEMSTACK);
                    if(isADS(stack)){
                        return state.setAndContinue(RawAnimation.begin().thenLoop("ads_idle"));
                    }
                    if(hasBayonet && !isBayonet(stack)){
                        return state.setAndContinue(RawAnimation.begin().thenLoop("idle_nobayonet"));
                    }
                    return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                }).triggerableAnim(name+"_sighton",
                        RawAnimation.begin().thenPlay("sighton"))
                .triggerableAnim(name+"_sightoff",
                        RawAnimation.begin().thenPlay("sightoff"))
        );
    }
    public static void anipush(ItemStack stack,String key,int tick){
        addKstack(stack,key);
        addTstack(stack,tick);
    }
    public static void posanipush(ItemStack stack,String key,int tick){
        addPKstack(stack,key);
        addPTstack(stack,tick);
    }
    public static GunItem getGunClass(ItemStack stack){
        return (GunItem)stack.getItem();
    }
    public static int getAmmo(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("ammo")) {
            stack.getOrCreateTag().putInt("ammo",getGunClass(stack).maxAmmo);
        }
        return stack.getOrCreateTag().getInt("ammo");
    }
    public static void setAmmo(ItemStack stack, int ammo) {
        stack.getOrCreateTag().putInt("ammo", ammo);
    }
    public static boolean isBusy(ItemStack stack) {
        return !stack.getOrCreateTag().getList("tstack",Tag.TAG_INT).isEmpty();
    }
    public static boolean isPoseBusy(ItemStack stack) {
        return !stack.getOrCreateTag().getList("ptstack",Tag.TAG_INT).isEmpty();
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 0;
    }
    @Override
    public boolean shouldCauseReequipAnimation(
            ItemStack oldStack,
            ItemStack newStack,
            boolean slotChanged
    ) {
        return false;
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if(!level.isClientSide() && isSelected && entity instanceof Player){
            ListTag timeStack=stack.getOrCreateTag().getList("tstack",Tag.TAG_INT);
            ListTag posetimeStack=stack.getOrCreateTag().getList("ptstack",Tag.TAG_INT);
            if (!timeStack.isEmpty()){
                int lockedtime=getLocked(stack);
                String animKey=readKstack(stack);
                if(lockedtime==0){
                    if(animKey.equals("fire")){
                        long id = GeoItem.getOrAssignId(stack, (ServerLevel) level);

                        AnimatableManager<?> manager=getAnimatableInstanceCache()
                                        .getManagerForId(id);
                        AnimationController<?> controller =
                                manager.getAnimationControllers()
                                        .get(controllername);
                        controller.forceAnimationReset();
                    }
                    triggerAnim(entity,GeoItem.getOrAssignId(stack, (ServerLevel) level),controllername,name+"_"+animKey);
                    setLocked(stack,readTstack(stack));
                    lockedtime=readTstack(stack);
                }
                setLocked(stack,lockedtime-1);
                if (lockedtime==1){
                    if(animKey.equals("reload")){
                        setAmmo(stack,getGunClass(stack).ammoinput);
                        setBoltAction(stack,false);
                    }
                    if(animKey.equals("bolt")){
                        setBoltAction(stack,false);
                    }
                    if(animKey.equals("bayoneton")){
                        setBayonet(stack,true);
                    }
                    if(animKey.equals("bayonetoff")){
                        setBayonet(stack,false);
                    }
                    removeTstack(stack);
                    removeKstack(stack);
                }
            }
            if (!posetimeStack.isEmpty()){
                int lockedtime=getPLocked(stack);
                String animKey=readPKstack(stack);
                if(lockedtime==0){
                    triggerAnim(entity,GeoItem.getOrAssignId(stack, (ServerLevel) level),pcontrollername,name+"_"+animKey);
                    setPLocked(stack,readPTstack(stack));
                    lockedtime=readPTstack(stack);
                }
                setPLocked(stack,lockedtime-1);
                if (lockedtime==1){
                    if(animKey.equals("sighton")){
                        setADS(stack,true);
                    }
                    if(animKey.equals("sightoff")){
                        setADS(stack,false);
                    }
                    removePTstack(stack);
                    removePKstack(stack);
                }
            }
        }
    }
    public static int getLocked(ItemStack stack) {
        return stack.getOrCreateTag().getInt("lockedtime");
    }
    public static int getPLocked(ItemStack stack) {
        return stack.getOrCreateTag().getInt("plockedtime");
    }
    public static void removeTstack(ItemStack stack){
        ListTag t=stack.getOrCreateTag().getList("tstack",Tag.TAG_INT);
        if (!t.isEmpty()){
            t.remove(0);
        }
    }
    public static void removeKstack(ItemStack stack){
        ListTag t=stack.getOrCreateTag().getList("kstack",Tag.TAG_STRING);
        if (!t.isEmpty()){
            t.remove(0);
        }
    }
    public static int readTstack(ItemStack stack) {
        ListTag t = stack.getOrCreateTag().getList("tstack", Tag.TAG_INT);
        if (!t.isEmpty()) {
            return ((IntTag)t.get(0)).getAsInt();
        }
        return 0;
    }
    public static String readKstack(ItemStack stack) {
        ListTag t = stack.getOrCreateTag().getList("kstack", Tag.TAG_STRING);
        if (!t.isEmpty()) {
            return ((StringTag)t.get(0)).getAsString();
        }
        return "";
    }
    public static void addTstack(ItemStack stack,int i){
        CompoundTag tag=stack.getOrCreateTag();
        if(!tag.contains("tstack")){
            tag.put("tstack", new ListTag());
        }
        ListTag t=tag.getList("tstack",Tag.TAG_INT);
        t.add(IntTag.valueOf(i));
    }
    public static void addKstack(ItemStack stack,String i){
        CompoundTag tag=stack.getOrCreateTag();
        if(!tag.contains("kstack")){
            tag.put("kstack", new ListTag());
        }
        ListTag t=tag.getList("kstack",Tag.TAG_STRING);
        t.add(StringTag.valueOf(i));
    }
    public static void removePTstack(ItemStack stack){
        ListTag t=stack.getOrCreateTag().getList("ptstack",Tag.TAG_INT);
        if (!t.isEmpty()){
            t.remove(0);
        }
    }
    public static void removePKstack(ItemStack stack){
        ListTag t=stack.getOrCreateTag().getList("pkstack",Tag.TAG_STRING);
        if (!t.isEmpty()){
            t.remove(0);
        }
    }
    public static int readPTstack(ItemStack stack) {
        ListTag t = stack.getOrCreateTag().getList("ptstack", Tag.TAG_INT);
        if (!t.isEmpty()) {
            return ((IntTag)t.get(0)).getAsInt();
        }
        return 0;
    }
    public static String readPKstack(ItemStack stack) {
        ListTag t = stack.getOrCreateTag().getList("pkstack", Tag.TAG_STRING);
        if (!t.isEmpty()) {
            return ((StringTag)t.get(0)).getAsString();
        }
        return "";
    }
    public static void addPTstack(ItemStack stack,int i){
        CompoundTag tag=stack.getOrCreateTag();
        if(!tag.contains("ptstack")){
            tag.put("ptstack", new ListTag());
        }
        ListTag t=tag.getList("ptstack",Tag.TAG_INT);
        t.add(IntTag.valueOf(i));
    }
    public static void addPKstack(ItemStack stack,String i){
        CompoundTag tag=stack.getOrCreateTag();
        if(!tag.contains("pkstack")){
            tag.put("pkstack", new ListTag());
        }
        ListTag t=tag.getList("pkstack",Tag.TAG_STRING);
        t.add(StringTag.valueOf(i));
    }
    public static boolean isNeedBoltAction(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("boltaction");
    }
    public static boolean isADS(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("ADS");
    }
    public static void setADS(ItemStack stack,boolean t) {
        stack.getOrCreateTag().putBoolean("ADS",t);
    }
    public static boolean isBayonet(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Bayonet");
    }
    public static void setBayonet(ItemStack stack,boolean t) {
        stack.getOrCreateTag().putBoolean("Bayonet",t);
    }

    public static void setBoltAction(ItemStack stack, boolean t){
        if(isBoltActionType(stack)) {
            stack.getOrCreateTag().putBoolean("boltaction", t);
        }
    }
    public static void setLocked(ItemStack stack, int t) {
        stack.getOrCreateTag().putInt("lockedtime", t);
    }
    public static void setPLocked(ItemStack stack, int t) {
        stack.getOrCreateTag().putInt("plockedtime", t);
    }
    public static void doReload(ServerPlayer player,ItemStack stack){
        var gun=getGunClass(stack);
        int amount=utility.BulletAmount(player,gun.bulletType);
        int ammonow=getAmmo(stack);
        if(amount>0 && gun.maxAmmo!=ammonow){
            int consume=Math.min(amount,gun.maxAmmo-ammonow);
            //maxAmmo-nowAmmo
            gun.ammoinput=consume;
            anipush(stack, "reload", gun.reloadTick);
            utility.consumeBullet(player,consume,gun.bulletType);
        }
    }
    public static void doBolt(ItemStack stack){
        anipush(stack,"bolt",getGunClass(stack).boltTick);
    }
    public static void doBayonetSwitch(ItemStack stack){
        var gun = getGunClass(stack);
        if(gun.hasBayonet) {
            if(isBayonet(stack)){
                anipush(stack,"bayonetoff",40);
            }else{
                anipush(stack,"bayoneton",40);
            }
        }
    }
    public static void doAttack(ServerPlayer player,ItemStack stack){
        var gun = getGunClass(stack);
        if(gun.hasBayonet) {
            utility.bayonetAttack(player);
            anipush(stack,"attack",25);
        }
    }
    public static void doFire(ItemStack stack){
        if(getGunClass(stack).name.equals("flintlock_musket")) {
            CameraShakeHandler.shake(5, 5f);
            NetworkHandler.INSTANCE.sendToServer(new KeyPacket("musket_smoke"));
        }else {
            CameraShakeHandler.shake(2, 4f);
        }
        anipush(stack,"fire",getGunClass(stack).fireTick);
    }
    public static boolean isBoltActionType(ItemStack stack) {
        return getGunClass(stack).boltaction;
    }
}