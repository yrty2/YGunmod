package com.yirty2.ygunmod.Items;

import com.yirty2.ygunmod.Items.GunItem;
import com.yirty2.ygunmod.Items.guns.BoltactionRifle;
import com.yirty2.ygunmod.Items.guns.MachineRifle;
import net.minecraft.world.item.Item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.yirty2.ygunmod.Ygunmod.MODID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    ForgeRegistries.ITEMS,
                    MODID
            );
    public static final RegistryObject<Item> Smokelesspowder=
            ITEMS.register(
                    "smokelesspowder",() -> new Item(new Item.Properties()
                            .stacksTo(64)
                    )
            );
    public static final RegistryObject<Item> BULLET_ITEM =
            ITEMS.register(
                    "bullet",() -> new Item(new Item.Properties()
                            .stacksTo(64)
                    )
            );
    public static final RegistryObject<Item> BALLBULLET_ITEM =
            ITEMS.register(
                    "ballbullet",() -> new Item(new Item.Properties()
                            .stacksTo(64)
                    )
            );
    public static final RegistryObject<Item> BOLTACTION_RIFLE =
            ITEMS.register(
                    "boltaction_rifle",() -> new BoltactionRifle(new Item.Properties().stacksTo(1),"boltaction_rifle",10,43,5,4.0,15)
            );
    public static final RegistryObject<Item> MACHINE_RIFLE =
            ITEMS.register(
                    "machine_rifle",() -> new MachineRifle(new Item.Properties().stacksTo(1),"machine_rifle",2,55,25,4.0,0)
            );//model Beretta M1918
    public static final RegistryObject<Item> FLINTLOCK_MUSKET =
            ITEMS.register(
                    "flintlock_musket",() -> new GunItem(new Item.Properties().stacksTo(1),"flintlock_musket",12,210,1,12.0,BALLBULLET_ITEM.get(),true,1.5f,0)
            );
    /*public static final RegistryObject<Item> MATCHLOCK_MUSKET =
            ITEMS.register(
                    "matchlock_musket",() -> new GunItem(new Item.Properties().stacksTo(1),"matchlock_musket",12,25,5,4.0,0)
            );
    public static final RegistryObject<Item> ARQUEBUS=
            ITEMS.register(
                    "arquebus",() -> new GunItem(new Item.Properties().stacksTo(1),"arquebus",25,900,5,12.0,0)
            );*/
}