package com.mohistmc.banner.mixin.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftSmithingTransformRecipe;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SmithingTransformRecipe.class)
public abstract class MixinSmithingTransformRecipe implements SmithingRecipe {

    @Shadow @Final
    Ingredient template;

    @Shadow @Final
    Ingredient base;

    @Shadow @Final
    Ingredient addition;

    @Shadow @Final private ResourceLocation id;

    @Shadow @Final
    ItemStack result;

    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.result);
        CraftSmithingTransformRecipe recipe = new CraftSmithingTransformRecipe(CraftNamespacedKey.fromMinecraft(this.id), result, CraftRecipe.toBukkit(this.template), CraftRecipe.toBukkit(this.base), CraftRecipe.toBukkit(this.addition));
        return recipe;
    }
}
