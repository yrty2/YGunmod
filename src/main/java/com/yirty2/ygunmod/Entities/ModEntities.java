package com.yirty2.ygunmod.Entities;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.yirty2.ygunmod.Ygunmod.MODID;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register("bullet",
            () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.15F, 0.15F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("bullet")
    );
}