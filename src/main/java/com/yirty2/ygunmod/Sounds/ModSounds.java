package com.yirty2.ygunmod.Sounds;

import com.yirty2.ygunmod.Ygunmod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Ygunmod.MODID);

    public static final RegistryObject<SoundEvent> GARANDSHOOT =
            register("garandshoot");

    public static final RegistryObject<SoundEvent> BOLTOPEN =
            register("boltopen");

    public static final RegistryObject<SoundEvent> BOLTCLOSE =
            register("boltclose");

    public static final RegistryObject<SoundEvent> RIFLERELOAD =
            register("riflereload");
    public static final RegistryObject<SoundEvent> RIFLE =
            register("rifle");
    public static final RegistryObject<SoundEvent> BATTLERIFLE =
            register("battlerifle");
    public static final RegistryObject<SoundEvent> BULLET_HIT =
            register("bullet_hit");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.parse(Ygunmod.MODID+":"+name)
                ));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}