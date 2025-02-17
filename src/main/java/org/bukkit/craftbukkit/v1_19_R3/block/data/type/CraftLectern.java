package org.bukkit.craftbukkit.v1_19_R3.block.data.type;

import org.bukkit.block.data.type.Lectern;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;

public abstract class CraftLectern extends CraftBlockData implements Lectern {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty HAS_BOOK = getBoolean("has_book");

    @Override
    public boolean hasBook() {
        return get(HAS_BOOK);
    }
}
