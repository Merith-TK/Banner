package com.mohistmc.banner.mixin.server.dedicated;

import com.mojang.datafixers.DataFixer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.ConsoleInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.io.IoBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_19_R3.util.ForwardLogHandler;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    @Shadow @Final public RconConsoleSource rconConsoleSource;

    public MixinDedicatedServer(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, levelStorageAccess, packRepository, worldStem, proxy, dataFixer, services, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer",
            at = @At(value = "INVOKE",
            target = "Ljava/lang/Thread;setDaemon(Z)V",
            ordinal = 0,
            shift = At.Shift.BEFORE))
    private void banner$addLog4j(CallbackInfoReturnable<Boolean> cir) {
        // CraftBukkit start - TODO: handle command-line logging arguments
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        global.addHandler(new ForwardLogHandler());
        final org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();

        System.setOut(IoBuilder.forLogger(logger).setLevel(org.apache.logging.log4j.Level.INFO).buildPrintStream());
        System.setErr(IoBuilder.forLogger(logger).setLevel(org.apache.logging.log4j.Level.WARN).buildPrintStream());
        // CraftBukkit end
    }

    @Inject(method = "initServer", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/rcon/thread/RconThread;create(Lnet/minecraft/server/ServerInterface;)Lnet/minecraft/server/rcon/thread/RconThread;"))
    public void banner$setRcon(CallbackInfoReturnable<Boolean> cir) {
        this.banner$setRemoteConsole(new CraftRemoteConsoleCommandSender(this.rconConsoleSource));
    }

    @Inject(method = "getPluginNames", at = @At("RETURN"), cancellable = true)
    private void banner$setPluginNames(CallbackInfoReturnable<String> cir) {
        StringBuilder result = new StringBuilder();
        org.bukkit.plugin.Plugin[] plugins = bridge$server().getPluginManager().getPlugins();

        result.append(bridge$server().getName());
        result.append(" on Bukkit ");
        result.append(bridge$server().getBukkitVersion());

        if (plugins.length > 0 && bridge$server().getQueryPlugins()) {
            result.append(": ");

            for (int i = 0; i < plugins.length; i++) {
                if (i > 0) {
                    result.append("; ");
                }

                result.append(plugins[i].getDescription().getName());
                result.append(" ");
                result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
            }
        }

        cir.setReturnValue(result.toString());
    }

    @Redirect(method = "handleConsoleInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;performPrefixedCommand(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)I"))
    private int banner$serverCommandEvent(Commands commands, CommandSourceStack source, String command) {
        if (command.isEmpty()) {
            return 0;
        }
        ServerCommandEvent event = new ServerCommandEvent(bridge$console(), command);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            bridge$server().dispatchServerCommand(bridge$console(), new ConsoleInput(event.getCommand(), source));
        }
        return 0;
    }

    /**
     * @author wdog5
     * @reason
     */
    @Overwrite
    public String runCommand(String command) {
        this.rconConsoleSource.prepareForCommand();
        this.executeBlocking(() -> {
            RemoteServerCommandEvent event = new RemoteServerCommandEvent(bridge$remoteConsole(), command);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            this.bridge$server().dispatchServerCommand(bridge$remoteConsole(), new ConsoleInput(event.getCommand(), this.rconConsoleSource.createCommandSourceStack()));
        });
        return this.rconConsoleSource.getCommandResponse();
    }

    @Inject(method = "onServerExit", at = @At("RETURN"))
    public void banner$exitNow(CallbackInfo ci) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            } finally {
                Runtime.getRuntime().halt(0);
            }
        }, "Exit Thread").start();
        System.exit(0);
    }

    @Override
    public CommandSender getBukkitSender(CommandSourceStack wrapper) {
        return bridge$console();
    }
}
