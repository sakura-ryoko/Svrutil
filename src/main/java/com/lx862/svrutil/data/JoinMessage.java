package com.lx862.svrutil.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.Util;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class JoinMessage {
    public Text title;
    public Text subtitle;
    public Text joinMessage;
    public int delayTick;
    public String permApiNode;
    public List<Integer> permLevel;

    public JoinMessage(Text title, Text subtitle, Text joinMessage, int delayTick, List<Integer> permLevel) {
        this.title = title;
        this.subtitle = subtitle;
        this.joinMessage = joinMessage;
        this.delayTick = delayTick;
        this.permLevel = permLevel;
    }

    public void show(ServerPlayerEntity player) {
        if (!permLevel.isEmpty() && !permLevel.contains(Util.getPermLevel(player)))
            return;

        Util.showWelcomeMessage(player, this);
    }

    public static JoinMessage fromJson(JsonObject jsonObject) {
        List<Integer> permLevels = new ArrayList<>();
        Text title = jsonObject.has("title") ? Text.Serialization.fromJsonTree(jsonObject.get("title"), ModInfo.registryManager) : null;
        Text subtitle = jsonObject.has("subtitle") ? Text.Serialization.fromJsonTree(jsonObject.get("subtitle"), ModInfo.registryManager) : null;
        Text joinMessage = jsonObject.has("message") ? Text.Serialization.fromJsonTree(jsonObject.get("message"), ModInfo.registryManager)
                : null;
        int delayTick = jsonObject.has("delayTick") ? jsonObject.get("delayTick").getAsInt() : 0;

        try {
            jsonObject.get("permLevels").getAsJsonArray().forEach(e -> {
                permLevels.add(e.getAsInt());
            });
        } catch (Exception ignored) {
        }

        return new JoinMessage(title, subtitle, joinMessage, delayTick, permLevels);
    }

    public static JsonObject toJson(JoinMessage joinMessage) {
        JsonObject jsonObject = new JsonObject();
        JsonArray permLevels = new JsonArray();
        for (Integer i : joinMessage.permLevel) {
            permLevels.add(i);
        }
        jsonObject.add("title", TextCodecs.CODEC.encodeStart(ModInfo.registryManager.getOps(JsonOps.INSTANCE), joinMessage.title).getOrThrow());
        jsonObject.add("subtitle", TextCodecs.CODEC.encodeStart(ModInfo.registryManager.getOps(JsonOps.INSTANCE), joinMessage.subtitle).getOrThrow());
        jsonObject.add("message", TextCodecs.CODEC.encodeStart(ModInfo.registryManager.getOps(JsonOps.INSTANCE), joinMessage.joinMessage).getOrThrow());
        jsonObject.addProperty("delayTick", joinMessage.delayTick);
        jsonObject.add("permLevels", permLevels);
        return jsonObject;
    }
}
