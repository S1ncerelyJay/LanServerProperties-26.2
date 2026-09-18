package rikka.lanserverproperties.mixin;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rikka.lanserverproperties.LanServerProperties;

@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer {
    @Inject(method = "getMaxPlayers", at = @At("HEAD"), cancellable = true)
    private void lsp$getMaxPlayers(CallbackInfoReturnable<Integer> cir) {
        if (LanServerProperties.customMaxPlayers > 0) {
            cir.setReturnValue(LanServerProperties.customMaxPlayers);
        }
    }

    @Inject(method = "publishServer(Lnet/minecraft/server/MinecraftServer$MultiplayerScope;I)Z", at = @At("HEAD"))
    private void lsp$onPublishServer(MinecraftServer.MultiplayerScope scope, int port, CallbackInfoReturnable<Boolean> cir) {
        if (scope == MinecraftServer.MultiplayerScope.LAN) {
            LanServerProperties.applyToServer((IntegratedServer) (Object) this);
        }
    }
}