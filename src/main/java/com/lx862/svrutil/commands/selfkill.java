package com.lx862.svrutil.commands;

import static com.lx862.svrutil.ModInfo.*;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class selfkill {
    private static final CommandEntry defaultEntry = new CommandEntry("suicide", 0, MOD_ID + ".command.suicide", true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if (!entry.enabled)
            return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(Permissions.require(entry.permApiNode, entry.permLevel))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    player.kill();
                    Mappings.broadcast(context.getSource().getServer().getPlayerManager(), player,
                            Mappings.literalText(player.getDisplayName().getString() + " took their own life.")
                                    .formatted(Formatting.RED));
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                }));
    }
}
