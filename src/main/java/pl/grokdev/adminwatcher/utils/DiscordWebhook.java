package pl.grokdev.adminwatcher.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class DiscordWebhook {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void send(String webhookUrl, String content) {
        if (webhookUrl == null || webhookUrl.isEmpty()) return;

        try {
            // Prosty webhook – nie chcemy tu komplikować życia
            String json = "{\"content\": \"" + escapeJson(content) + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            // Cicho – nie spamujemy konsoli przy błędzie Discorda
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}