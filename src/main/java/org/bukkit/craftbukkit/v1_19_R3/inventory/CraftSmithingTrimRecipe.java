package org.bukkit.craftbukkit.v1_19_R3.inventory;

import com.mohistmc.banner.util.ServerUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTrimRecipe;

public class CraftSmithingTrimRecipe extends SmithingTrimRecipe implements CraftRecipe {

    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        super(key, template, base, addition);
    }

    public static CraftSmithingTrimRecipe fromBukkitRecipe(SmithingTrimRecipe recipe) {
        if (recipe instanceof CraftSmithingTrimRecipe) {
            return (CraftSmithingTrimRecipe) recipe;
        }
        CraftSmithingTrimRecipe ret = new CraftSmithingTrimRecipe(recipe.getKey(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition());
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        ServerUtils.getServer().getRecipeManager().addRecipe(new net.minecraft.world.item.crafting.SmithingTrimRecipe(CraftNamespacedKey.toMinecraft(this.getKey()), toNMS(this.getTemplate(), true), toNMS(this.getBase(), true), toNMS(this.getAddition(), true)));
    }
}
