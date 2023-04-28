package com.mohistmc.banner.injection.world.level.block;

import net.minecraft.server.level.ServerLevel;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftPortalEvent;
import org.jetbrains.annotations.Nullable;

public interface InjectionPortalInfo {

    default void banner$setPortalEventInfo(CraftPortalEvent event){
    }

    default CraftPortalEvent bridge$getPortalEventInfo() {
        return null;
    }

    default void banner$setWorld(ServerLevel world){
    }

    default @Nullable ServerLevel bridge$getWorld(){
        return null;
    }
}
