package com.lx862.svrutil.commands;

import static com.lx862.svrutil.ModInfo.*;

import com.lx862.svrutil.ModInfo;
import me.lucko.fabric.api.permissions.v0.Permissions;
import java.util.Collection;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class fancyKick {
    private static final CommandEntry defaultEntry = new CommandEntry("fancykick", 4, MOD_ID + ".command.fancykick",
            true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if (!entry.enabled)
            return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(Permissions.require(entry.permApiNode, entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                        .then(CommandManager.argument("reason", TextArgumentType.text(CommandRegistryAccess.of(ModInfo.registryManager, ModInfo.featureSet)))
                                .executes(context -> {
                                    Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context,
                                            "target");
                                    Text text = TextArgumentType.getTextArgument(context, "reason");

                                    for (ServerPlayerEntity player : targets) {
                                        player.networkHandler.disconnect(text);
                                        Mappings.sendFeedback(context, Text.translatable("commands.kick.success",
                                                player.getDisplayName(), text), true);
                                    }

                                    Commands.finishedExecution(context, defaultEntry);
                                    return 1;
                                }))));
    }
}
