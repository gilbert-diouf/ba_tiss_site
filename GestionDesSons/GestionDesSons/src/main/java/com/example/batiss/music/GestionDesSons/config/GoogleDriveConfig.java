package com.example.batiss.music.GestionDesSons.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive driveService() throws Exception{
        InputStream in = GoogleDriveConfig.class.getResourceAsStream("/credentials.json");
        GoogleCredential credential=GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Batiss Music").build();
    }
}