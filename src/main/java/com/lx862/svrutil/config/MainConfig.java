package com.lx862.svrutil.config;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Config.getConfigPath("config.json");
    private static Text silentKickMessage = Mappings.literalText("Internal Exception: java.lang.StackOverflowError");
    public static Text whitelistedMessage = null;
    public static final List<JoinMessage> joinMessages = new ArrayList<>();

    public static boolean load() {
        if (!Files.exists(CONFIG_PATH)) {
            SvrUtilMain.LOGGER.warn("[{}] Main Config not found, generating one...", ModInfo.MOD_ID);
            generate();
            load();
            return true;
        }

        SvrUtilMain.LOGGER.info("[{}] Reading config...", ModInfo.MOD_ID);
        joinMessages.clear();
        try {
            //final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH)))
                    //.getAsJsonObject();
            final JsonObject jsonConfig = JsonParser.parseReader(new InputStreamReader(new FileInputStream(CONFIG_PATH.toFile()), StandardCharsets.UTF_8)).getAsJsonObject();

            if (jsonConfig.has("joinMessages")) {
                jsonConfig.getAsJsonArray("joinMessages").forEach(e -> {
                    JsonObject jsonObject = e.getAsJsonObject();
                    joinMessages.add(JoinMessage.fromJson(jsonObject));
                });
            }

            if (jsonConfig.has("whitelistedMessage")) {
                JsonElement element = jsonConfig.get("whitelistedMessage");
                try {
                    whitelistedMessage = Text.of(element.getAsString());
                } catch (Exception ignored) {
                }
            }

            if (jsonConfig.has("silentKickMessage")) {
                JsonElement element = jsonConfig.get("silentKickMessage");
                try {
                    silentKickMessage = Text.of(element.getAsString());
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            SvrUtilMain.LOGGER.error("load(): Main Config load error: [{}]", e.getMessage());
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
                20, Arrays.asList(0, 1, 2, 3, 4)));
        welcomeConfig.add(welcomeConfig1);
        jsonConfig.addProperty("whitelistedMessage", "You are not whitelisted on the server!");
        jsonConfig.addProperty("silentKickMessage", "Internal Exception: java.lang.StackOverflowError");
        jsonConfig.add("joinMessages", welcomeConfig);

        try {
            Files.write(CONFIG_PATH,
                    Collections.singleton(GSON.toJson(jsonConfig)));
        } catch (Exception e) {
            SvrUtilMain.LOGGER.error("generate(): Main Config generate error: [{}]", e.getMessage());
        }
    }

    public static Text getSilentKickMessage() {
        return silentKickMessage;
    }
}
