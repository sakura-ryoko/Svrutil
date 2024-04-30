package com.lx862.svrutil.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.gson.*;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.SvrUtilMain;
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
            SvrUtilMain.LOGGER.warn("[{}] Config file not found, generating one...", ModInfo.MOD_ID);
            generate();
            load();
            return true;
        }

        SvrUtilMain.LOGGER.info("[{}] Reading config...", ModInfo.MOD_ID);
        joinMessages.clear();
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH)))
                    .getAsJsonObject();

            if (jsonConfig.has("joinMessages")) {
                jsonConfig.getAsJsonArray("joinMessages").forEach(e -> {
                    JsonObject jsonObject = e.getAsJsonObject();

                    SvrUtilMain.LOGGER.info("load(): joinMessages object: {}", jsonObject.toString());
                    joinMessages.add(JoinMessage.fromJson(jsonObject));
                });
            }

            if (jsonConfig.has("whitelistedMessage")) {
                JsonElement element = jsonConfig.get("whitelistedMessage");
                try {
                    whitelistedMessage = Text.Serialization.fromJson(element.getAsString(), ModInfo.registryManager);
                } catch (Exception ignored) {
                }

                try {
                    whitelistedMessage = Text.Serialization.fromJsonTree(element, ModInfo.registryManager);
                } catch (Exception ignored) {
                }
            }

            if (jsonConfig.has("silentKickMessage")) {
                JsonElement element = jsonConfig.get("silentKickMessage");
                try {
                    silentKickMessage = Text.Serialization.fromJson(element.getAsString(), ModInfo.registryManager);
                } catch (Exception ignored) {
                }

                try {
                    silentKickMessage = Text.Serialization.fromJsonTree(element, ModInfo.registryManager);
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
        SvrUtilMain.LOGGER.info("[{}] Generating config...", ModInfo.MOD_ID);
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
