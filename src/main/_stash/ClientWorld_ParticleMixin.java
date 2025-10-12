package com.sah.noflamesmoke.mixin;

import com.sah.noflamesmoke.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorld_ParticleMixin {

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V",
            at = @At("HEAD"), cancellable = true)
    private void nfs$onAddParticle(ParticleEffect effect,
                                   double x, double y, double z,
                                   double vx, double vy, double vz,
                                   CallbackInfo ci) {
        if (shouldBlock((ClientWorld)(Object)this, effect, x, y, z)) ci.cancel();
    }

    @Inject(method = "addImportantParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V",
            at = @At("HEAD"), cancellable = true)
    private void nfs$onAddImportantParticle(ParticleEffect effect,
                                            double x, double y, double z,
                                            double vx, double vy, double vz,
                                            CallbackInfo ci) {
        if (shouldBlock((ClientWorld)(Object)this, effect, x, y, z)) ci.cancel();
    }

    private static boolean shouldBlock(ClientWorld world, ParticleEffect effect,
                                       double x, double y, double z) {
        ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (cfg.disableAll) return true;

        final boolean isFlame = effect == ParticleTypes.FLAME
                || effect == ParticleTypes.SMALL_FLAME
                || effect == ParticleTypes.SOUL_FIRE_FLAME;

        final boolean isSmoke = effect == ParticleTypes.SMOKE
                || effect == ParticleTypes.LARGE_SMOKE
                || effect == ParticleTypes.CAMPFIRE_COSY_SMOKE
                || effect == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;

        if (!((cfg.disableFlame && isFlame) || (cfg.disableSmoke && isSmoke))) return false;
        if (!cfg.onlySelectedBlocks) return true; // global mute

        BlockPos base = BlockPos.ofFloored(x, y, z);
        BlockPos below = base.down();

        if (matchAt(world, base, cfg) || matchAt(world, below, cfg)) return true;
        for (Direction d : Direction.Type.HORIZONTAL) {
            if (matchAt(world, base.offset(d), cfg)) return true;
        }
        return false;
    }

    private static boolean matchAt(ClientWorld w, BlockPos p, ModConfig cfg) {
        BlockState st = w.getBlockState(p);
        Block b = st.getBlock();

        if (cfg.affectTorches) {
            if (b instanceof TorchBlock || b instanceof WallTorchBlock
                || b instanceof RedstoneTorchBlock || b instanceof WallRedstoneTorchBlock) return true;
        }
        if (cfg.affectSoulVariants) {
            if (b == Blocks.SOUL_TORCH || b == Blocks.SOUL_WALL_TORCH) return true;
            if (b instanceof CampfireBlock && st.getOrEmpty(Properties.SIGNAL_FIRE).orElse(false)) return true;
        }
        if (cfg.affectCampfires) {
            if (b instanceof CampfireBlock && st.getOrEmpty(Properties.LIT).orElse(false)) return true;
        }
        if (cfg.affectCandles) {
            if ((b instanceof CandleBlock || b instanceof CandleCakeBlock)
                && st.getOrEmpty(Properties.LIT).orElse(false)) return true;
        }
        if (cfg.affectFurnaces) {
            if ((b instanceof FurnaceBlock || b instanceof SmokerBlock || b instanceof BlastFurnaceBlock)
                && st.getOrEmpty(Properties.LIT).orElse(false)) return true;
        }
        if (cfg.affectLanterns) {
            if (b instanceof LanternBlock) return true;
        }
        return false;
    }
}
