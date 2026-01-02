package com.rzero.drownedandtrident.entity.override.DATThrownTrident;

import com.rzero.drownedandtrident.infrastructure.enchantmentTriggerType.ModEnchantmentHelper;
import com.rzero.drownedandtrident.item.DATItemFunctionRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class DATThrownTrident extends ThrownTrident {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(DATThrownTrident.class, EntityDataSerializers.BYTE);
    private static final Logger log = LoggerFactory.getLogger(DATThrownTrident.class);
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;
    private boolean hadBeenHit = false;

    // 常规（未对tick到秒的转换进行调整）情况下，1秒CD所需的冷却tick数
//    private int onNormalSecondEnchantmentAppliedCoolDown = 0;
    private float velocity;

    public DATThrownTrident(EntityType<? extends ThrownTrident> entityType, Level level) {
        super(entityType, level);
    }

    public DATThrownTrident(Level level, LivingEntity shooter, ItemStack pickupItemStack) {
        super(level, shooter, pickupItemStack);
    }

    public DATThrownTrident(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(level, x, y, z, pickupItemStack);
    }

    @Override
    public void tick() {
        super.tick();
        if (!(this.level() instanceof ServerLevel serverlevel)) {
            return;
        }
        if (!hadBeenHit) {
            ModEnchantmentHelper.onEntityTick(
                    serverlevel,
                    this,
                    this.getWeaponItem(),
                    new Vec3(this.getX(), this.getY(), this.getZ())
            );
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte)0);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 8.0F;
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
        if (this.level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
        }

        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource, this.getWeaponItem());
                // 同时应用绑定了Hitblock触发器的附魔，三叉戟原生没有任何绑定了hitblock触发器的附魔，可以只执行自定义的
                EnchantmentHelper.onHitBlock(
                        serverlevel1,
                        this.getWeaponItem(),
                        this.getOwner() instanceof LivingEntity livingentity ? livingentity : null,
                        this,
                        null,
                        result.getLocation(),
                        serverlevel1.getBlockState(result.getEntity().getOnPos()),
                        item -> this.kill());
            }

            if (entity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);

        // stop sign for on_entity_tick
        this.hadBeenHit = true;
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        EnchantmentHelper.onHitBlock(
                level,
                stack,
                this.getOwner() instanceof LivingEntity livingentity ? livingentity : null,
                this,
                null,
                vec3,
                level.getBlockState(hitResult.getBlockPos()),
                p_348680_ -> this.kill()
        );

        // stop sign for on_entity_tick
        this.hadBeenHit = true;
    }

    /**
     * apply entity on init enchantment
     */
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy, ServerLevel level, Vec3 shootPos){
        this.velocity = velocity;
        ModEnchantmentHelper.doAccelerateEffects(level, this, this.getWeaponItem(), shootPos);
        shootFromRotation(shooter, x, y, z, this.velocity, inaccuracy);
    }


    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((x + z) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));

        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        Vec3 vec3 = shooter.getKnownMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(DATItemFunctionRegister.DAT_TRIDENT_ITEM.get());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127)
                : 0;
    }


    public static final EntityType<DATThrownTrident> DAT_THROWN_TRIDENT =
            EntityType.Builder.<DATThrownTrident>of(DATThrownTrident::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("dat_thrown_trident");


    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
