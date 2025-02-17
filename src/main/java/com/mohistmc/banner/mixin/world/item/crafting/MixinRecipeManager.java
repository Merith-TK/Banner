package com.mohistmc.banner.mixin.world.item.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mohistmc.banner.injection.world.item.crafting.InjectionRecipeManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager implements InjectionRecipeManager {

    @Shadow private Map<ResourceLocation, Recipe<?>> byName;

    @Shadow public Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

    @Shadow protected abstract <C extends Container, T extends Recipe<C>> Map<ResourceLocation, T> byType(RecipeType<T> recipeType);

    @Shadow @Final private static Logger LOGGER;

    @Shadow
    public static Recipe<?> fromJson(ResourceLocation recipeId, JsonObject json) {
        return null;
    }

    @Shadow private boolean hasErrors;

    /**
     * @author wdog5
     * @reason
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        this.hasErrors = false;
        Map<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>> map = Maps.newHashMap();

        for (RecipeType<?> type : BuiltInRegistries.RECIPE_TYPE) {
            map.put(type, new Object2ObjectLinkedOpenHashMap<>());
        }

        ImmutableMap.Builder<ResourceLocation, Recipe<?>> builder = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_"))
                continue; //Forge: filter anything beginning with "_" as it's used for metadata.

            try {
                if (entry.getValue().isJsonObject()) {
                    LOGGER.debug("Skipping loading recipe {} as it's conditions were not met", resourcelocation);
                    continue;
                }
                Recipe<?> irecipe = fromJson(resourcelocation, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
                if (irecipe == null) {
                    LOGGER.info("Skipping loading recipe {} as it's serializer returned null", resourcelocation);
                    continue;
                }
                map.computeIfAbsent(irecipe.getType(), (recipeType) -> new Object2ObjectLinkedOpenHashMap<>())
                        .putAndMoveToFirst(resourcelocation, irecipe);
                builder.put(resourcelocation, irecipe);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading recipe {}", resourcelocation, jsonparseexception);
            }
        }

        this.recipes = (Map) map;
        this.byName = Maps.newHashMap(builder.build());
        LOGGER.info("Loaded {} recipes", map.size());
    }

    /**
     * @author wdog5
     * @reason
     */
    @Overwrite
    public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> recipeTypeIn, C inventoryIn, Level worldIn) {
        Optional<T> optional = this.byType(recipeTypeIn).values().stream().filter((recipe) -> {
            return recipe.matches(inventoryIn, worldIn);
        }).findFirst();
        inventoryIn.setCurrentRecipe(optional.orElse(null));
        return optional;
    }

    @Override
    public void addRecipe(Recipe<?> recipe) {
        if (this.recipes instanceof ImmutableMap) {
            this.recipes = new HashMap<>(recipes);
        }
        if (this.byName instanceof ImmutableMap) {
            this.byName = new HashMap<>(byName);
        }
        Map<ResourceLocation, Recipe<?>> original = this.recipes.get(recipe.getType());
        Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>> map;
        if (!(original instanceof Object2ObjectLinkedOpenHashMap)) {
            Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>> hashMap = new Object2ObjectLinkedOpenHashMap<>();
            hashMap.putAll(original);
            this.recipes.put(recipe.getType(), hashMap);
            map = hashMap;
        } else {
            map = ((Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>) original);
        }

        if (this.byName.containsKey(recipe.getId()) || map.containsKey(recipe.getId())) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + recipe.getId());
        } else {
            map.putAndMoveToFirst(recipe.getId(), recipe);
            this.byName.put(recipe.getId(), recipe);
        }
    }

    @Override
    public boolean removeRecipe(ResourceLocation mcKey) {
        for (var recipes : recipes.values()) {
            recipes.remove(mcKey);
        }
        return byName.remove(mcKey) != null;
    }

    @Override
    public void clearRecipes() {
        this.recipes = new HashMap<>();
        for (RecipeType<?> type : BuiltInRegistries.RECIPE_TYPE) {
            this.recipes.put(type, new Object2ObjectLinkedOpenHashMap<>());
        }
        this.byName = new HashMap<>();
    }

}
