package com.example.batiss.music.GestionDesSons.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor
@Service
public class GoogleDriveService {

    private final Drive driveService;

    /**
     * Génère un lien de téléchargement pour un fichier Drive
     */
    public String generateDownloadLink(String fileId) throws IOException{
        File file= driveService.files().get(fileId).setFields("webViewLink, webContentLink").execute();
        return file.getWebContentLink(); // lien direct de téléchargement
    }

    /**
     * Donne l’accès en lecture à un utilisateur
     */

    public void shareFileWithUser(String fileId, String userEmail) throws IOException{
        Permission permission=new Permission()
                .setType("user")
                .setRole("reader")
                .setEmailAddress(userEmail);

        driveService.permissions().create(fileId,permission).execute();
    }
}
