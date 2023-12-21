package com.lx862.svrutil.mixin;

import java.net.SocketAddress;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lx862.svrutil.SvrUtilMain;
import com.lx862.svrutil.config.MainConfig;
import com.mojang.authlib.GameProfile;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

/*
 * This Mixin is used for sending fake time packets to client (/clienttime), and overriding the whitelist message.
 */
@Mixin(value = PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow
    @Final
    private List<ServerPlayerEntity> players;

    @Inject(method = "sendToDimension", at = @At("HEAD"), cancellable = true)
    public void sendToDimension(Packet<?> packet, RegistryKey<World> dimension,
            CallbackInfo ci) {
        if (packet instanceof WorldTimeUpdateS2CPacket) {
            for (ServerPlayerEntity serverPlayerEntity : players) {
                if (serverPlayerEntity.getWorld().getRegistryKey() == dimension
                        && !SvrUtilMain.fakeTimeList.containsKey(serverPlayerEntity.getUuid())) {
                    serverPlayerEntity.networkHandler.sendPacket(packet);
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "checkCanJoin", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void checkCanJoinWhitelist(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        if (MainConfig.whitelistedMessage != null) {
            cir.setReturnValue(MainConfig.whitelistedMessage);
            cir.cancel();
        }
    }
}