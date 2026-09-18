package rikka.lanserverproperties.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.MultiplayerOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rikka.lanserverproperties.IPUtils;
import rikka.lanserverproperties.IntegerEditBox;
import rikka.lanserverproperties.LanServerProperties;
import rikka.lanserverproperties.OnlineMode;
import rikka.lanserverproperties.UUIDFixer;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(MultiplayerOptionsScreen.class)
public abstract class MixinMultiplayerOptionsScreen extends Screen {
    @Shadow @Final private HeaderAndFooterLayout layout;
    @Shadow private Button applyChanges;
    @Shadow private EditBox portEdit;
    @Shadow private int port;
    @Shadow private MinecraftServer.MultiplayerScope initialMultiplayerScope;

    @Unique private CycleButton<OnlineMode> lsp$onlineModeButton;
    @Unique private CycleButton<Boolean> lsp$pvpButton;
    @Unique private IntegerEditBox lsp$maxPlayersBox;

    protected MixinMultiplayerOptionsScreen(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void lsp$injectWidgets(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        IntegratedServer server = mc.getSingleplayerServer();

        if (this.initialMultiplayerScope != MinecraftServer.MultiplayerScope.LAN) {
            if (LanServerProperties.preferences.enablePreference) {
                LanServerProperties.currentOnlineMode = OnlineMode.of(
                        LanServerProperties.preferences.onlineMode,
                        LanServerProperties.preferences.fixUUID
                );
                LanServerProperties.currentPvp = LanServerProperties.preferences.allowPVP;
                LanServerProperties.customMaxPlayers = LanServerProperties.preferences.maxPlayer;
                if (this.portEdit != null) {
                    this.port = LanServerProperties.preferences.defaultPort;
                    this.portEdit.setValue(String.valueOf(LanServerProperties.preferences.defaultPort));
                }
            }
        } else if (server != null) {
            LanServerProperties.currentOnlineMode = OnlineMode.of(server.usesAuthentication(), UUIDFixer.tryOnlineFirst);
            LanServerProperties.currentPvp = server.getGameRules().get(GameRules.PVP);
            LanServerProperties.customMaxPlayers = server.getMaxPlayers();
        }

        FrameLayout contentsFrame = ((HeaderAndFooterLayoutAccessor) this.layout).getContentsFrame();
        AtomicReference<LinearLayout> contentLayoutRef = new AtomicReference<>();
        contentsFrame.visitChildren(elem -> {
            if (elem instanceof LinearLayout ll && contentLayoutRef.get() == null) {
                contentLayoutRef.set(ll);
            }
        });
        LinearLayout contentLayout = contentLayoutRef.get();
        if (contentLayout == null) {
            return;
        }

        LinearLayout row1 = LinearLayout.horizontal().spacing(8);
        row1.defaultCellSetting().alignHorizontallyCenter();

        this.lsp$onlineModeButton = CycleButton.builder(
                (OnlineMode mode) -> mode.stateName,
                LanServerProperties.currentOnlineMode
        )
        .withValues(OnlineMode.values())
        .withTooltip(mode -> Tooltip.create(mode.tooltip))
        .create(
                Component.translatable("lanserverproperties.options.online_mode"),
                (btn, val) -> {
                    LanServerProperties.currentOnlineMode = val;
                    if (server != null && server.isPublished()) {
                        server.setUsesAuthentication(val.onlineModeEnabled);
                        UUIDFixer.tryOnlineFirst = val.tryOnlineUUIDFirst;
                    }
                }
        );

        this.lsp$pvpButton = CycleButton.onOffBuilder(LanServerProperties.currentPvp)
        .create(
                Component.translatable("lanserverproperties.gui.pvp_allowed"),
                (btn, val) -> {
                    LanServerProperties.currentPvp = val;
                    if (server != null) {
                        server.getGameRules().set(GameRules.PVP, val, server);
                    }
                }
        );

        row1.addChild(this.lsp$onlineModeButton);
        row1.addChild(this.lsp$pvpButton);
        contentLayout.addChild(row1);

        LinearLayout row2 = LinearLayout.horizontal().spacing(8);
        row2.defaultCellSetting().alignHorizontallyCenter();

        LinearLayout maxPlayersLayout = LinearLayout.horizontal().spacing(4);
        maxPlayersLayout.defaultCellSetting().alignHorizontallyCenter();

        StringWidget maxPlayersLabel = new StringWidget(
                Component.translatable("lanserverproperties.gui.max_player"),
                this.font
        );
        this.lsp$maxPlayersBox = new IntegerEditBox(
                this.font,
                0, 0, 60, 20,
                Component.translatable("lanserverproperties.gui.max_player"),
                LanServerProperties.customMaxPlayers,
                box -> {
                    if (box.isContentValid()) {
                        LanServerProperties.customMaxPlayers = box.getValueAsInt(8);
                    }
                },
                IntegerEditBox.makeValidator(1, 256),
                null
        );
        maxPlayersLayout.addChild(maxPlayersLabel);
        maxPlayersLayout.addChild(this.lsp$maxPlayersBox);

        String localIp = IPUtils.getPrimaryLocalIP();
        Button copyIpBtn = Button.builder(
                Component.literal("IP: " + localIp),
                btn -> {
                    int p = this.port;
                    String ipPort = localIp + ":" + p;
                    mc.keyboardHandler.setClipboard(ipPort);
                    btn.setMessage(Component.translatable("lanserverproperties.button.ip_copied", ipPort));
                }
        ).width(150).build();
        copyIpBtn.setTooltip(Tooltip.create(Component.literal("点击复制本机局域网地址和端口")));

        row2.addChild(maxPlayersLayout);
        row2.addChild(copyIpBtn);
        contentLayout.addChild(row2);

        LinearLayout row3 = LinearLayout.horizontal().spacing(8);
        row3.defaultCellSetting().alignHorizontallyCenter();

        Button savePrefBtn = Button.builder(
                Component.translatable("lanserverproperties.button.preference_save"),
                btn -> {
                    LanServerProperties.preferences.onlineMode = LanServerProperties.currentOnlineMode.onlineModeEnabled;
                    LanServerProperties.preferences.fixUUID = LanServerProperties.currentOnlineMode.tryOnlineUUIDFirst;
                    LanServerProperties.preferences.allowPVP = LanServerProperties.currentPvp;
                    LanServerProperties.preferences.maxPlayer = LanServerProperties.customMaxPlayers;
                    LanServerProperties.preferences.defaultPort = this.port;
                    LanServerProperties.preferences.save();
                    btn.setMessage(Component.translatable("lanserverproperties.button.preference_saved"));
                }
        ).width(150).build();

        CycleButton<Boolean> autoLoadBtn = CycleButton.onOffBuilder(LanServerProperties.preferences.enablePreference)
                .withTooltip(val -> Tooltip.create(Component.translatable("lanserverproperties.options.preference_enabled.message")))
                .create(
                        Component.translatable("lanserverproperties.options.preference_enabled"),
                        (btn, val) -> {
                            LanServerProperties.preferences.enablePreference = val;
                            LanServerProperties.preferences.save();
                        }
                );

        row3.addChild(savePrefBtn);
        row3.addChild(autoLoadBtn);
        contentLayout.addChild(row3);

        this.addRenderableWidget(this.lsp$onlineModeButton);
        this.addRenderableWidget(this.lsp$pvpButton);
        this.addRenderableWidget(maxPlayersLabel);
        this.addRenderableWidget(this.lsp$maxPlayersBox);
        this.addRenderableWidget(copyIpBtn);
        this.addRenderableWidget(savePrefBtn);
        this.addRenderableWidget(autoLoadBtn);

        this.repositionElements();
    }

    @Inject(method = "publish", at = @At("HEAD"))
    private void lsp$onPublish(IntegratedServer server, MinecraftServer.MultiplayerScope scope, CallbackInfo ci) {
        if (scope == MinecraftServer.MultiplayerScope.LAN) {
            LanServerProperties.applyToServer(server);
            if (LanServerProperties.preferences.enablePreference) {
                LanServerProperties.preferences.onlineMode = LanServerProperties.currentOnlineMode.onlineModeEnabled;
                LanServerProperties.preferences.fixUUID = LanServerProperties.currentOnlineMode.tryOnlineUUIDFirst;
                LanServerProperties.preferences.allowPVP = LanServerProperties.currentPvp;
                LanServerProperties.preferences.maxPlayer = LanServerProperties.customMaxPlayers;
                LanServerProperties.preferences.defaultPort = this.port;
                LanServerProperties.preferences.save();
            }
        }
    }

    @Inject(method = "lambda$init$2", at = @At("HEAD"))
    private void lsp$onApplyChanges(IntegratedServer server, Button button, CallbackInfo ci) {
        LanServerProperties.applyToServer(server);
        if (LanServerProperties.preferences.enablePreference) {
            LanServerProperties.preferences.onlineMode = LanServerProperties.currentOnlineMode.onlineModeEnabled;
            LanServerProperties.preferences.fixUUID = LanServerProperties.currentOnlineMode.tryOnlineUUIDFirst;
            LanServerProperties.preferences.allowPVP = LanServerProperties.currentPvp;
            LanServerProperties.preferences.maxPlayer = LanServerProperties.customMaxPlayers;
            LanServerProperties.preferences.defaultPort = this.port;
            LanServerProperties.preferences.save();
        }
    }
}