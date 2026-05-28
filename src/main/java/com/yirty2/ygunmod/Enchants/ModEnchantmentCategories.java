package com.yirty2.ygunmod.Enchants;

import com.yirty2.ygunmod.Items.GunItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantmentCategories {
    public static final EnchantmentCategory GUN =
            EnchantmentCategory.create(
                    "gun",
                    item -> item instanceof GunItem
            );
}