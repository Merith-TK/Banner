package com.mohistmc.banner.mixin.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FriendlyByteBuf.class)
public abstract class MixinFriendlyByteBuf {

    @ModifyExpressionValue(method = "writeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean modifyReturn(ItemStack stack) {
        return stack.isEmpty() || stack.getItem() == null;
    }

    @Inject(method = "writeItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/FriendlyByteBuf;writeNbt(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/network/FriendlyByteBuf;",
                    shift = At.Shift.AFTER))
    private void banner$setItemMeta(ItemStack stack, CallbackInfoReturnable<FriendlyByteBuf> cir) {
        // CraftBukkit start
        if (stack.getTag() != null) {
            CraftItemStack.setItemMeta(stack, CraftItemStack.getItemMeta(stack));
        }
        // CraftBukkit end
    }
}
