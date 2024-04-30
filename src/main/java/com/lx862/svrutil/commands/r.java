package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.SvrUtilMain;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import me.lucko.fabric.api.permissions.v0.Permissions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import static com.lx862.svrutil.ModInfo.MOD_ID;

public class r {
        private static final CommandEntry defaultEntry = new CommandEntry("r", 0, MOD_ID + ".command.r", true);

        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
                final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
                if (!entry.enabled)
                        return;

                dispatcher.register(CommandManager.literal(entry.commandName)
                                .requires(Permissions.require(entry.permApiNode, entry.permLevel))
                                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                        String player = context.getSource().getName();
                                                        ServerPlayerEntity target = context.getSource().getServer()
                                                                        .getPlayerManager()
                                                                        .getPlayer(SvrUtilMain.lastReply.get(player));
                                                        if (target == null) {
                                                                Mappings.sendFeedback(context,
                                                                                Mappings.literalText(
                                                                                                "Either no one have replied to you, or the player is offline.")
                                                                                                .formatted(Formatting.GRAY),
                                                                                false);
                                                                return 1;
                                                        }

                                                        if (context.getSource().getWorld()
                                                                        .getPlayerByUuid(target.getUuid()) == null) {
                                                                Mappings.sendFeedback(context,
                                                                                Mappings.literalText(
                                                                                                "The player you were messaging with are now offline")
                                                                                                .formatted(Formatting.RED),
                                                                                false);
                                                                return 1;
                                                        }

                                                        String message = StringArgumentType.getString(context,
                                                                        "message");
                                                        Mappings.sendFeedback(context,
                                                                        Mappings.literalText(String.format(
                                                                                        "§6[me §r-> §6%s]: §r%s",
                                                                                        target.getGameProfile()
                                                                                                        .getName(),
                                                                                        message)),
                                                                        false);
                                                        target.sendMessage(
                                                                        Mappings.literalText(String.format(
                                                                                        "§6[%s §r-> §6me]: §r%s",
                                                                                        player, message)),
                                                                        false);
                                                        target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
                                                        SvrUtilMain.lastReply.put(target.getGameProfile().getName(),
                                                                        player);
                                                        Commands.finishedExecution(context, defaultEntry);
                                                        return 1;
                                                })));
        }
}
