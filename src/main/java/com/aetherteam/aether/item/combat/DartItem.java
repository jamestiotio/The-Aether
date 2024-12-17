package com.aetherteam.aether.item.combat;

import com.aetherteam.aether.entity.projectile.dart.AbstractDart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class DartItem extends ArrowItem implements ProjectileItem {
    public DartItem(Properties properties) {
        super(properties);
    }

    /**
     * Creates Dart with setup using shooter entity.
     *
     * @param level   {@link Level} of shooter entity.
     * @param shooter {@link LivingEntity} shooting dart.
     * @return {@link AbstractDart} entity created from dart entity type.
     */
    public abstract AbstractDart createDart(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack firedFromWeapon);

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack weapon, LivingEntity livingEntity) {
        return weapon.getEnchantmentLevel(livingEntity.level().holderOrThrow(Enchantments.INFINITY)) > 0;
    }
}
