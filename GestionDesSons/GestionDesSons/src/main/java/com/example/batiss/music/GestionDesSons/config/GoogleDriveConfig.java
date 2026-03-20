package com.example.batiss.music.GestionDesSons.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive driveService() throws Exception {
        InputStream in;

        File secretFile1 = new File("/etc/secrets/credentials.json");
        File secretFile2 = new File("credentials.json");
        File secretFile3 = new File("/app/credentials.json");

        // Logs pour déboguer
        System.out.println("=== GOOGLE DRIVE DEBUG ===");
        System.out.println("Chemin 1 existe: " + secretFile1.exists() + " → " + secretFile1.getAbsolutePath());
        System.out.println("Chemin 2 existe: " + secretFile2.exists() + " → " + secretFile2.getAbsolutePath());
        System.out.println("Chemin 3 existe: " + secretFile3.exists() + " → " + secretFile3.getAbsolutePath());
        System.out.println("ResourceAsStream existe: " + (GoogleDriveConfig.class.getResourceAsStream("/credentials.json") != null));

        if (secretFile1.exists()) {
            in = new FileInputStream(secretFile1);
        } else if (secretFile2.exists()) {
            in = new FileInputStream(secretFile2);
        } else if (secretFile3.exists()) {
            in = new FileInputStream(secretFile3);
        } else {
            in = GoogleDriveConfig.class.getResourceAsStream("/credentials.json");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Batiss Music").build();
    }
}