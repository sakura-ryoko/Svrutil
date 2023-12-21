package com.lx862.svrutil.commands;

import static com.lx862.svrutil.ModInfo.*;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.config.MainConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class silentKick {
    private static final CommandEntry defaultEntry = new CommandEntry("silentkick", 4, MOD_ID + ".command.silentkick",
            true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if (!entry.enabled)
            return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(Permissions.require(entry.permApiNode, entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> {
                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                            target.networkHandler.disconnect(MainConfig.getSilentKickMessage());
                            Commands.finishedExecution(context, defaultEntry);
                            return 1;
                        })));
    }
}
