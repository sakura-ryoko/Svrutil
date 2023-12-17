package com.lx862.svrutil.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.data.JoinMessage;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MainConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("config.json");
    private static Text silentKickMessage = Mappings.literalText("Internal Exception: java.lang.StackOverflowError");
    public static Text whitelistedMessage = null;
    public static final List<JoinMessage> joinMessages = new ArrayList<>();

    public static boolean load() {
        if (!Files.exists(CONFIG_PATH)) {
            SvrUtil.LOGGER.warn("[{}] Config file not found, generating one...", ModInfo.MOD_ID);
            generate();
            load();
            return true;
        }

        SvrUtil.LOGGER.info("[{}] Reading config...", ModInfo.MOD_ID);
        joinMessages.clear();
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH)))
                    .getAsJsonObject();

            if (jsonConfig.has("joinMessages")) {
                jsonConfig.getAsJsonArray("joinMessages").forEach(e -> {
                    JsonObject jsonObject = e.getAsJsonObject();
                    joinMessages.add(JoinMessage.fromJson(jsonObject));
                });
            }

            if (jsonConfig.has("whitelistedMessage")) {
                JsonElement element = jsonConfig.get("whitelistedMessage");
                try {
                    whitelistedMessage = Text.Serializer.fromJson(element.getAsString());
                } catch (Exception ignored) {
                }

                try {
                    whitelistedMessage = Text.Serializer.fromJson(element);
                } catch (Exception ignored) {
                }
            }

            if (jsonConfig.has("silentKickMessage")) {
                JsonElement element = jsonConfig.get("silentKickMessage");
                try {
                    silentKickMessage = Text.Serializer.fromJson(element.getAsString());
                } catch (Exception ignored) {
                }

                try {
                    silentKickMessage = Text.Serializer.fromJson(element);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            generate();
            return false;
        }

        return true;
    }

    public static void generate() {
        SvrUtil.LOGGER.info("[{}] Generating config...", ModInfo.MOD_ID);
        final JsonObject jsonConfig = new JsonObject();
        final JsonArray welcomeConfig = new JsonArray();
        final JsonObject welcomeConfig1 = JoinMessage.toJson(new JoinMessage(
                Mappings.literalText("Welcome title").formatted(Formatting.GREEN),
                Mappings.literalText("Welcome subtitle").formatted(Formatting.AQUA),
                Mappings.literalText(
                        "Please edit \"config/svrutil-lite/config.json\" to change the welcome message.\n\nThank you for installing SvrUtil-Lite.")
                        .formatted(Formatting.GREEN),
                20, Arrays.asList(1, 2, 3, 4)));
        welcomeConfig.add(welcomeConfig1);
        jsonConfig.addProperty("whitelistedMessage", "You are not whitelisted on the server!");
        jsonConfig.addProperty("silentKickMessage", "Internal Exception: java.lang.StackOverflowError");
        jsonConfig.add("joinMessages", welcomeConfig);

        try {
            Files.write(CONFIG_PATH,
                    Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Text getSilentKickMessage() {
        return silentKickMessage;
    }
}
