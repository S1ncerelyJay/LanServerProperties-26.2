package rikka.lanserverproperties;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.gamerules.GameRules;

public class LanServerProperties {
    public static int customMaxPlayers = 8;
    public static OnlineMode currentOnlineMode = OnlineMode.OFFLINE_FIX_UUID;
    public static boolean currentPvp = true;
    public static Preferences preferences = Preferences.read();

    public static void applyToServer(IntegratedServer server) {
        if (server == null) return;
        server.setUsesAuthentication(currentOnlineMode.onlineModeEnabled);
        server.getGameRules().set(GameRules.PVP, currentPvp, server);
        UUIDFixer.tryOnlineFirst = currentOnlineMode.tryOnlineUUIDFirst;
        UUIDFixer.alwaysOfflinePlayers = preferences.playersAlwaysOffline;
    }
}