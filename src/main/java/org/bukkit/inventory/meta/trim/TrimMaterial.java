package org.bukkit.inventory.meta.trim;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.MinecraftExperimental;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a material that may be used in an {@link ArmorTrim}.
 *
 * @apiNote Armor trims are part of an experimental feature of Minecraft and
 * hence subject to change. Constants in this class may be null if a data pack
 * is not present to enable these features.
 */
@MinecraftExperimental
@ApiStatus.Experimental
public interface TrimMaterial extends Keyed {

    /**
     * {@link Material#QUARTZ}.
     */
    public static final TrimMaterial QUARTZ = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("quartz"));
    /**
     * {@link Material#IRON_INGOT}.
     */
    public static final TrimMaterial IRON = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("iron"));
    /**
     * {@link Material#NETHERITE_INGOT}.
     */
    public static final TrimMaterial NETHERITE = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("netherite"));
    /**
     * {@link Material#REDSTONE}.
     */
    public static final TrimMaterial REDSTONE = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("redstone"));
    /**
     * {@link Material#COPPER_INGOT}.
     */
    public static final TrimMaterial COPPER = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("copper"));
    /**
     * {@link Material#GOLD_INGOT}.
     */
    public static final TrimMaterial GOLD = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("gold"));
    /**
     * {@link Material#EMERALD}.
     */
    public static final TrimMaterial EMERALD = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("emerald"));
    /**
     * {@link Material#DIAMOND}.
     */
    public static final TrimMaterial DIAMOND = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("diamond"));
    /**
     * {@link Material#LAPIS_LAZULI}.
     */
    public static final TrimMaterial LAPIS = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("lapis"));
    /**
     * {@link Material#AMETHYST_SHARD}.
     */
    public static final TrimMaterial AMETHYST = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("amethyst"));

    @NotNull
    @Override
    public NamespacedKey getKey(); // Satisfies Checkstyle for now
}
