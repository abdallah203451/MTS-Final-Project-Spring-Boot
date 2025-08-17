package com.example.WorkforceManagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Component
public class SwaggerUiAutoOpen implements CommandLineRunner {

    private final Environment env;

    public SwaggerUiAutoOpen(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        boolean autoOpen = Boolean.parseBoolean(env.getProperty("swagger.auto-open", "true"));
        if (!autoOpen) return;

        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html");
        String url = "http://localhost:" + port + (path.startsWith("/") ? path : ("/" + path));

        // The UI is often at /swagger-ui/index.html — opening the alias should redirect
        // Run in a separate thread so it doesn't block app startup.
        CompletableFuture.runAsync(() -> {
            try {
                // Small delay to let the server finish starting (adjust if needed)
                Thread.sleep(Duration.ofSeconds(1).toMillis());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    // Not supported (headless), just print where to open manually
                    System.out.println("Swagger UI available at: " + url);
                }
            } catch (Exception e) {
                // swallow exceptions - if headless or cannot open browser we still run normally
                System.out.println("Swagger UI available at: " + url + " (auto-open failed: " + e.getMessage() + ")");
            }
        });
    }
}
