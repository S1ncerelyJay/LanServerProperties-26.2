package rikka.lanserverproperties;

import net.minecraft.network.chat.Component;

public enum OnlineMode {
    ONLINE(true, false, "on"),
    OFFLINE_FIX_UUID(false, true, "off.fixed"),
    OFFLINE(false, false, "off.vanilla");

    public final boolean onlineModeEnabled;
    public final boolean tryOnlineUUIDFirst;
    public final Component stateName;
    public final Component tooltip;

    OnlineMode(boolean onlineModeEnabled, boolean tryOnlineUUIDFirst, String key) {
        this.onlineModeEnabled = onlineModeEnabled;
        this.tryOnlineUUIDFirst = tryOnlineUUIDFirst;
        this.stateName = Component.translatable("lanserverproperties.options.online_mode." + key);
        this.tooltip = Component.translatable("lanserverproperties.options.online_mode." + key + ".message");
    }

    public static OnlineMode of(boolean onlineModeEnabled, boolean tryOnlineUUIDFirst) {
        if (onlineModeEnabled) {
            return ONLINE;
        }
        return tryOnlineUUIDFirst ? OFFLINE_FIX_UUID : OFFLINE;
    }
}