package com.yirty2.ygunmod.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class GunEnchants extends Enchantment {
    public final int maxLevel;
    public GunEnchants(int maxlevel) {
        super(
                Rarity.RARE,
                ModEnchantmentCategories.GUN,
                new EquipmentSlot[]{
                        EquipmentSlot.MAINHAND
                }
        );
        maxLevel=maxlevel;
    }
    @Override
    public int getMaxLevel() {
        return maxLevel;
    }
}