package com.yirty2.ygunmod.Items.guns;

import com.yirty2.ygunmod.Items.GunItem;

import static com.yirty2.ygunmod.Items.ModItems.BULLET_ITEM;

public class BoltactionRifle extends GunItem {
    public BoltactionRifle(Properties properties, String namestr, int ft, int rt, int ammo, double dam, int bt) {
        super(properties, namestr, ft, rt, ammo, dam,BULLET_ITEM.get(),false,0.1f, bt);
    }
}
