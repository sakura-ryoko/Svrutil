package com.lx862.svrutil;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import com.lx862.svrutil.config.MainConfig;
import com.lx862.svrutil.data.JoinMessage;

public class Events {
    public static void onServerStart(MinecraftServer server)
    {
        ModInfo.setRegistryManager(server.getRegistryManager().toImmutable());

        /* Register Fabric API Events */
        ServerPlayConnectionEvents.JOIN.register(Events::onJoin);
        ServerTickEvents.START_SERVER_TICK.register(Events::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(Events::onTickEnd);
    }


    public static void onServerStarted(MinecraftServer server)
    {
        if (!ModInfo.registryManager.equals(server.getRegistryManager()))
        {
            ModInfo.setRegistryManager(server.getRegistryManager());
        }
        if (!ModInfo.featureSet.equals(server.getCommandSource().getEnabledFeatures()))
        {
            ModInfo.setFeatures(server.getCommandSource().getEnabledFeatures());
        }
    }

    public static void onServerTick(MinecraftServer server) {
        TickManager.onTickUpdate();
    }

    public static void onTickEnd(MinecraftServer server) {
        if (server.getTicks() % 20 == 0) {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (SvrUtilMain.fakeTimeList.containsKey(player.getUuid())) {
                    WorldTimeUpdateS2CPacket packet = new WorldTimeUpdateS2CPacket(
                            SvrUtilMain.fakeTimeList.get(player.getUuid()),
                            SvrUtilMain.fakeTimeList.get(player.getUuid()), true);
                    player.networkHandler.sendPacket(packet);
                }
            });
        }
    }

    public static void onJoin(ServerPlayNetworkHandler dispatcher, PacketSender sender, MinecraftServer server) {
        SvrUtilMain.debug("onJoin() player [{}] // permLevel [{}]", dispatcher.getPlayer().getNameForScoreboard(), Util.getPermLevel(dispatcher.getPlayer()));
        for (JoinMessage joinMessage : MainConfig.joinMessages) {
            joinMessage.show(dispatcher.getPlayer());
        }
    }
}