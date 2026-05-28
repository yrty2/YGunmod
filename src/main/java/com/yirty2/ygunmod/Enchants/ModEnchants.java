package com.yirty2.ygunmod.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.yirty2.ygunmod.Ygunmod.MODID;

public class ModEnchants {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(
                    ForgeRegistries.ENCHANTMENTS,
                    MODID
            );

    public static final RegistryObject<Enchantment> FIREPOWER=
            ENCHANTMENTS.register(
                    "firepower",
                    ()->new GunEnchants(4)
            );
    public static final RegistryObject<Enchantment> CHARGE=
            ENCHANTMENTS.register(
                    "charge",
                    ()->new GunEnchants(5)
            );
    public static final RegistryObject<Enchantment> HUNTING=
            ENCHANTMENTS.register(
                    "hunting",
                    ()->new GunEnchants(3)
            );
    public static final RegistryObject<Enchantment> DEXTEROUS=
            ENCHANTMENTS.register(
                    "dexterous",
                    ()->new GunEnchants(4)
            );
}