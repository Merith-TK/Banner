package com.mohistmc.banner.mixin.network.connection;

import com.mohistmc.banner.injection.network.connection.InjectionConnection;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Connection.class)
public class MixinConnection implements InjectionConnection {

    @Shadow public Channel channel;
    public String hostname = ""; // CraftBukkit - add field

    @Override
    public String bridge$hostname() {
        return hostname;
    }

    @Override
    public void banner$setHostName(String hostName) {
        hostname = hostName;
    }
}
