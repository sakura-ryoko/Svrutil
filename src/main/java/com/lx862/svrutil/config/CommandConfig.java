package com.lx862.svrutil.config;

import static com.lx862.svrutil.ModInfo.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.SvrUtilMain;
import com.lx862.svrutil.data.CommandEntry;

public class CommandConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("commands.json");
    public static final HashMap<String, CommandEntry> commandEntries = new HashMap<>();

    public static boolean load() {
        if (!Files.exists(CONFIG_PATH)) {
            SvrUtilMain.LOGGER.warn("[{}] Command config file not found, generating one...", ModInfo.MOD_ID);
            generate();
            load();
            return true;
        }

        SvrUtilMain.LOGGER.info("[{}] Reading Command config...", ModInfo.MOD_ID);
        commandEntries.clear();
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH)))
                    .getAsJsonObject();

            if (jsonConfig.has("overrides")) {
                JsonObject commandConfig = jsonConfig.getAsJsonObject("overrides");
                commandConfig.entrySet().forEach(entries -> {
                    String originalCmdName = entries.getKey();
                    JsonObject jsonCommandEntry = entries.getValue().getAsJsonObject();
                    CommandEntry commandEntry = new CommandEntry(jsonCommandEntry);
                    commandEntries.put(originalCmdName, commandEntry);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            generate();
            return false;
        }

        return true;
    }

    public static void generate() {
        SvrUtilMain.LOGGER.info("[{}] Generating command config...", ModInfo.MOD_ID);
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("_COMMENT_1",
                "This config is used to define what commands are enabled/disabled, it's required permission level to use those commands and such");
        jsonConfig.addProperty("_COMMENT_2",
                "Unless you are not satisfied with the current command configuration, you don't need to modify this file.");
        jsonConfig.addProperty("_COMMENT_3",
                "Otherwise, remove the underscore at the front of '_overrides' below and you may override the default command configuration.");

        final JsonObject commandConfigs = new JsonObject();
        final JsonObject sampleConfig1 = new CommandEntry("feed_remapped_command", 2, MOD_ID + ".commands.feed", true)
                .toJson();
        commandConfigs.add("feed", sampleConfig1);
        jsonConfig.add("_overrides", commandConfigs);

        try {
            Files.write(CONFIG_PATH, Collections.singleton(
                    new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CommandEntry getCommandEntry(@NotNull CommandEntry defaultEntry) {
        if (commandEntries.containsKey(defaultEntry.commandName)) {
            return defaultEntry.apply(commandEntries.get(defaultEntry.commandName));
        } else {
            return defaultEntry;
        }
    }
}
