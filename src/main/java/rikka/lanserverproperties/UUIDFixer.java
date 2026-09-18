package rikka.lanserverproperties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UUIDFixer {
    public static boolean tryOnlineFirst = false;
    public static List<String> alwaysOfflinePlayers = Collections.emptyList();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public static UUID hookEntry(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return null;
        }
        if (alwaysOfflinePlayers.contains(playerName)) {
            return null;
        }
        if (tryOnlineFirst) {
            return getOfficialUUID(playerName);
        }
        return null;
    }

    public static UUID getOfficialUUID(String playerName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerName))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null && !response.body().isEmpty()) {
                JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
                String name = root.getAsJsonPrimitive("name").getAsString();
                String id = root.getAsJsonPrimitive("id").getAsString();
                if (name.equalsIgnoreCase(playerName) && id != null && id.length() == 32) {
                    long msb = Long.parseUnsignedLong(id.substring(0, 16), 16);
                    long lsb = Long.parseUnsignedLong(id.substring(16, 32), 16);
                    return new UUID(msb, lsb);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}