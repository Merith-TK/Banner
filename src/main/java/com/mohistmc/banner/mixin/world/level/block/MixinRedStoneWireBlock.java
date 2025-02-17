package com.mohistmc.banner.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RedStoneWireBlock.class)
public abstract class MixinRedStoneWireBlock {

    @Shadow protected abstract int calculateTargetStrength(Level level, BlockPos pos);

    @Redirect(method = "updatePowerStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;calculateTargetStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I"))
    public int banner$blockRedstone(RedStoneWireBlock redstoneWireBlock, Level world, BlockPos pos, Level world1, BlockPos pos1, BlockState state) {
        int i = this.calculateTargetStrength(world, pos);
        int oldPower = state.getValue(RedStoneWireBlock.POWER);
        if (oldPower != i) {
            BlockRedstoneEvent event = new BlockRedstoneEvent(CraftBlock.at(world, pos), oldPower, i);
            Bukkit.getPluginManager().callEvent(event);
            i = event.getNewCurrent();
        }
        return i;
    }
}
