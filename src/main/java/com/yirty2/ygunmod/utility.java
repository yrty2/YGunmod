package com.yirty2.ygunmod;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class utility {
    public static int BulletAmount(ServerPlayer player, Item Bullet) {
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(Bullet)) {
                total += stack.getCount();
            }
        }
        return total;
    }
    public static void consumeBullet(ServerPlayer player, int amount, Item Bullet){
        int remaining = amount;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is(Bullet))
                continue;
            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;
            if (remaining <= 0)
                break;
        }
    }
    public static void spawnMusketSmoke(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level))
            return;
        Vec3 look = player.getLookAngle();
        Vec3 headPos = player.getEyePosition()
                .add(look.scale(0.4));
        Vec3 bodyPos = player.position()
                .add(0, player.getBbHeight()*0.55, 0)
                .add(look.scale(2));
        level.sendParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                headPos.x, headPos.y, headPos.z,
                8,
                0.08,0.08,0.08,
                0.02
        );
        level.sendParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                bodyPos.x, bodyPos.y, bodyPos.z,
                6,
                0.12,0.12,0.12,
                0.01
        );
    }
    public static void bayonetAttack(ServerPlayer player){

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();

        double range = 6.6;

        Vec3 end = eye.add(look.scale(range));

        AABB box = player.getBoundingBox()
                .expandTowards(look.scale(range))
                .inflate(1.3);

        LivingEntity target = null;
        double bestDist = range*range;

        for(Entity e : player.level().getEntities(
                player,
                box,
                ent -> ent instanceof LivingEntity
                        && ent != player
                        && ent.isAlive()
        )){
            LivingEntity living=(LivingEntity)e;
            AABB hitbox = living.getBoundingBox().inflate(0.3);
            if(hitbox.clip(eye,end).isPresent()){
                double d = player.distanceToSqr(living);
                if(d<bestDist){
                    bestDist=d;
                    target=living;
                }
            }
        }
        if(target!=null){
            float damage = 6f;
            Vec3 v = player.getDeltaMovement();
            damage += (float)(v.length()*4.0);
            target.hurt(
                    player.damageSources().playerAttack(player),
                    damage
            );
            target.knockback(
                    0.7,
                    -look.x,
                    -look.z
            );
        }
    }
}
