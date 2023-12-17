package com.lx862.svrutil;

import java.util.function.Consumer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class Mappings {
    public static MutableText literalText(String content) {
        return Text.literal(content);
    }

    public static void broadcast(PlayerManager playerManager, PlayerEntity player, MutableText content) {
        playerManager.broadcast(content, false);
    }

    public static void registerCommand(Consumer<CommandDispatcher<ServerCommandSource>> callback) {
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT
                .register((dispatcher, dedicated, third) -> callback.accept(dispatcher));
    }

    public static void sendFeedback(CommandContext<ServerCommandSource> context, MutableText text,
            boolean broadcastToOps) {
        context.getSource().sendFeedback(() -> text, broadcastToOps);
    }
}
