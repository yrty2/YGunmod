package com.yirty2.ygunmod.Entities;

import com.yirty2.ygunmod.Sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BulletEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BulletEntity(EntityType<? extends BulletEntity> type, Level level) {
        super(type, level);
    }

    public BulletEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BULLET.get(), shooter, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();

        if (!this.level().isClientSide && target instanceof LivingEntity livingTarget) {
            boolean b = livingTarget.hurtMarked;

            DamageSource damageSource = this.damageSources().mobAttack((LivingEntity) this.getOwner());
            float actualDamage = (float) this.getBaseDamage()*4;
            boolean hasHurt = livingTarget.hurt(damageSource, actualDamage);

            if (hasHurt){
                livingTarget.setDeltaMovement(0,0,0);
                livingTarget.hurtMarked = b;
                livingTarget.invulnerableTime = 0;
                livingTarget.hurtTime = 0;
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.BULLET_HIT.get(),
                    net.minecraft.sounds.SoundSource.NEUTRAL,
                    0.5F,
                    1.2F+this.random.nextFloat()-0.5F
            );
            this.level().addParticle(
                    ParticleTypes.FIREWORK,
                    this.getX(), this.getY(), this.getZ(),
                    0,0,0
            );
            this.discard();
        }
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide) {
            BlockState state = this.level().getBlockState(result.getBlockPos());
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    state.getSoundType().getBreakSound(),
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0F, 1.0F+(this.random.nextFloat()-0.5f)*0.4f);
        }
        this.discard();
    }
    @Override
    public void tick(){
        super.tick();

        this.setDeltaMovement(
                this.getDeltaMovement().x,
                this.getDeltaMovement().y + 0.05*0.8,
                this.getDeltaMovement().z
        );

        if (this.level().isClientSide) {

            // 現在位置
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            var v = this.getDeltaMovement();
            for(int k=0; k<2; ++k){
                float a=2*random.nextFloat()-1;
                float b=2*random.nextFloat()-1;
                float c=2*random.nextFloat()-1;
                this.level().addParticle(
                        ParticleTypes.SMOKE,
                        x+a/4, y+b/4, z+c/4,
                        -v.x * 0.03,
                        -v.y * 0.03,
                        -v.z * 0.03
                );
            }
        }
    }
}