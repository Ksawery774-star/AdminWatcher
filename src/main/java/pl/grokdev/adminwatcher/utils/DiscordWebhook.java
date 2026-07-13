package pl.grokdev.adminwatcher.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Prosty async webhook do Discorda.
 * Nie blokuje głównego wątku.
 */
public class DiscordWebhook {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void send(String url, String content) {
        if (url == null || url.isEmpty()) return;

        try {
            String json = "{\"content\":\"" + escape(content) + "\"}";

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {
            // Cicho ignorujemy błędy webhooka
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}