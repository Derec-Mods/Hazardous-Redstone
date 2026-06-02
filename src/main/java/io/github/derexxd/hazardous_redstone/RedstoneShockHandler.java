package io.github.derexxd.hazardous_redstone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public final class RedstoneShockHandler {

    private static final Map<UUID, Long> LAST_SHOCK_TICK = new HashMap<>();
    private static final int SHOCK_COOLDOWN_TICKS = 10;

    private RedstoneShockHandler() {
    }

    public static void tick(LivingEntity entity) {
        if (!(entity.getWorld() instanceof ServerWorld world)) {
            return;
        }

        if (!entity.isAlive() || entity.isSpectator()) {
            return;
        }

        BlockPos steppingPos = entity.getSteppingPos();
        BlockPos entityPos = entity.getBlockPos();
        BlockState stateAtStepping = world.getBlockState(steppingPos);
        BlockState stateAtEntity = world.getBlockState(entityPos);

        float damageStepping = getShockDamage(stateAtStepping);
        float damageEntity = getShockDamage(stateAtEntity);
        float damage = Math.max(damageStepping, damageEntity);
        BlockPos sourcePos = damageStepping >= damageEntity ? steppingPos : entityPos;

        if (damage <= 0.0F) {
            return;
        }

        long worldTime = world.getTime();
        UUID entityId = entity.getUuid();
        Long lastShockTick = LAST_SHOCK_TICK.get(entityId);

        if (lastShockTick != null && worldTime - lastShockTick < SHOCK_COOLDOWN_TICKS) {
            return;
        }

        if (entity.damage(world.getDamageSources().generic(), damage)) {
            LAST_SHOCK_TICK.put(entityId, worldTime);

            // kb scales with damage so a bigger shock means u get bounced a little more
            double dx = entity.getX() - (sourcePos.getX() + 0.5D);
            double dz = entity.getZ() - (sourcePos.getZ() + 0.5D);
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len <= 1e-6) {
                dx = (Math.random() - 0.5D);
                dz = (Math.random() - 0.5D);
                len = Math.sqrt(dx * dx + dz * dz);
            }
            dx /= len;
            dz /= len;

            double knockbackStrength = Math.min(0.5D, 0.2D + (damage * 0.15D));
            entity.takeKnockback(knockbackStrength, dx, dz);
        }
    }

    private static float getShockDamage(BlockState blockState) {
        if (blockState.isOf(Blocks.REDSTONE_BLOCK)) {
            return 6.0F;
        }

        if (blockState.isOf(Blocks.REDSTONE_WIRE)) {
            int power = blockState.get(RedstoneWireBlock.POWER);
            if (power > 0) {
                return 0.5F + (power * 0.35F);
            }
        }

        return 0.0F;
    }
}