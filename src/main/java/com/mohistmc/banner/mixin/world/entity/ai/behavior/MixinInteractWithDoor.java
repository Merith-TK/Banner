package com.mohistmc.banner.mixin.world.entity.ai.behavior;

import io.izzel.arclight.mixin.Eject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.event.entity.EntityInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InteractWithDoor.class)
public class MixinInteractWithDoor {

    @Eject(method = "desc=/Z$/", require = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DoorBlock;setOpen(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Z)V"))
    private static void banner$openDoor1(DoorBlock instance, Entity entity, Level p_153167_, BlockState p_153168_, BlockPos pos, boolean p_153170_, CallbackInfoReturnable<Boolean> cir) {
        var event = new EntityInteractEvent(entity.getBukkitEntity(), CraftBlock.at(entity.getLevel(), pos));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
            return;
        }
        instance.setOpen(entity, p_153167_, p_153168_, pos, p_153170_);
    }
}
