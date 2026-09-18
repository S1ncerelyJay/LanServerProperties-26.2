package rikka.lanserverproperties.mixin;

import net.minecraft.core.UUIDUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rikka.lanserverproperties.UUIDFixer;

import java.util.UUID;

@Mixin(UUIDUtil.class)
public abstract class MixinUUIDUtil {
    @Inject(method = "createOfflinePlayerUUID", at = @At("HEAD"), cancellable = true)
    private static void lsp$createOfflineUUID(String name, CallbackInfoReturnable<UUID> cir) {
        UUID u = UUIDFixer.hookEntry(name);
        if (u != null) {
            cir.setReturnValue(u);
        }
    }
}