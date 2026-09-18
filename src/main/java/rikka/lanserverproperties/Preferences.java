package rikka.lanserverproperties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preferences {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String PREFERENCE_FILE = "lsp.json";

    public boolean enablePreference = true;
    public GameType gameMode = GameType.SURVIVAL;
    public boolean allowCheat = false;
    public int defaultPort = 25565;
    public boolean onlineMode = false;
    public boolean fixUUID = true;
    public boolean allowPVP = true;
    public int maxPlayer = 8;
    public List<String> playersAlwaysOffline = new ArrayList<>();

    public static Path getConfigFolder() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("config");
    }

    public static Path getFilePath() {
        return getConfigFolder().resolve(PREFERENCE_FILE);
    }

    public static String getAlwaysOfflineString(List<String> playerList) {
        if (playerList == null || playerList.isEmpty()) {
            return "";
        }
        return String.join(" ", playerList);
    }

    public static List<String> listOfAlwaysOffline(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(str.trim().split("\\s+")));
    }

    public boolean save() {
        try {
            Files.createDirectories(getConfigFolder());
            try (BufferedWriter writer = Files.newBufferedWriter(getFilePath())) {
                GSON.toJson(this, writer);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Preferences read() {
        try {
            if (Files.exists(getFilePath())) {
                try (BufferedReader reader = Files.newBufferedReader(getFilePath())) {
                    Preferences p = GSON.fromJson(reader, Preferences.class);
                    if (p != null) return p;
                }
            }
        } catch (NoSuchFileException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Preferences();
    }
}