package com.mohistmc.banner.injection.server.network;

import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.RelativeMovement;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public interface InjectionServerGamePacketListenerImpl {

    default CraftPlayer getCraftPlayer() {
        return null;
    }

    default void disconnect(String s) {
    }

    default void teleport(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {
    }

    default boolean teleport(double d0, double d1, double d2, float f, float f1, Set<RelativeMovement> set, PlayerTeleportEvent.TeleportCause cause) { // CraftBukkit - Return event status
        return false;
    }

    default void teleport(Location dest) {
    }

    default void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<RelativeMovement> set) {
    }

    default void chat(String s, PlayerChatMessage original, boolean async) {
    }

    default void handleCommand(String s) {
    }

    default boolean isDisconnected() {
        return false;
    }
}
