package com.rzero.drownedandtrident.entity.override.DATThrownTrident;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DATThrownTrident extends ThrownTrident {
    public DATThrownTrident(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(level, x, y, z, pickupItemStack);
    }

    public DATThrownTrident(Level level, LivingEntity shooter, ItemStack pickupItemStack) {
        super(level, shooter, pickupItemStack);
    }

    public DATThrownTrident(EntityType<? extends ThrownTrident> entityType, Level level) {
        super(entityType, level);
    }



}
