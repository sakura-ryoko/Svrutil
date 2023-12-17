package com.lx862.svrutil;

import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lx862.svrutil.config.Config;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class SvrUtil implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ModInfo.MOD_ID);
    public static final String[] motds = { "Have a great day!", "OwO What's this?", "made ya look", "Drink water!",
            "i use arch btw", "The mystery is of Wet and Dry. And where does the solution lie?", "Backup Regularly >.<",
            "I'll be back in a jiffy!", "Treasure everything before it's no longer there.",
            "3 Billion Devices runs Java!" };
    public static final HashMap<String, String> lastReply = new HashMap<>();
    public static final HashMap<UUID, Long> fakeTimeList = new HashMap<>();

    @Override
    public void onInitialize() {
        String motd = motds[(int) (System.currentTimeMillis() % motds.length)];
        LOGGER.info("[{}] {}-{}", ModInfo.MOD_ID, ModInfo.MOD_NAME, ModInfo.getVersion());
        LOGGER.info("[{}] {}", ModInfo.MOD_ID, motd);

        Config.loadAll();

        Mappings.registerCommand(Commands::register);

        /* Register Fabric API Events */
        ServerPlayConnectionEvents.JOIN.register(Events::onJoin);
        ServerTickEvents.START_SERVER_TICK.register(Events::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(Events::onTickEnd);
    }
}
