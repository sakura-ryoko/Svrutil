package com.lx862.svrutil;

import java.util.HashMap;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SvrUtilMain implements ModInitializer {
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
        LOGGER.info("[{}] Description: {}", ModInfo.MOD_ID, ModInfo.getDescription());
        LOGGER.info("[{}] Author: {}", ModInfo.MOD_ID, ModInfo.getAuthors());
        LOGGER.info("[{}] Homepage: {}", ModInfo.MOD_ID, ModInfo.getHomepage());
        LOGGER.info("[{}] {}", ModInfo.MOD_ID, motd);

        ServerLifecycleEvents.SERVER_STARTING.register(Events::onServerStart);
        ServerLifecycleEvents.SERVER_STARTED.register(Events::onServerStarted);
    }
}
