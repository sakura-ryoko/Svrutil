package com.lx862.svrutil.config;

import static com.lx862.svrutil.ModInfo.*;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.SvrUtilMain;
import com.lx862.svrutil.data.CommandEntry;

public class CommandConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
            //final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH)))
                    //.getAsJsonObject();
            final JsonObject jsonConfig = JsonParser.parseReader(new InputStreamReader(new FileInputStream(CONFIG_PATH.toFile()), StandardCharsets.UTF_8)).getAsJsonObject();


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
            SvrUtilMain.LOGGER.error("load(): Command Config load error: [{}]", e.getMessage());
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
        final JsonObject sampleConfig1 = new CommandEntry("feed", 2, MOD_ID + ".commands.feed", true)
                .toJson();
        commandConfigs.add("feed", sampleConfig1);
        jsonConfig.add("_overrides", commandConfigs);

        try {
            Files.write(CONFIG_PATH, Collections.singleton(GSON.toJson(jsonConfig)));
        } catch (Exception e) {
            SvrUtilMain.LOGGER.error("generate(): Command Config generate error: [{}]", e.getMessage());
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
