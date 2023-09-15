package com.github.bibenga.palabras.firebase;

import java.io.FileInputStream;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class FirebaseAdminApplication {

    public static void main(String[] args) {
        var context = new SpringApplicationBuilder(FirebaseAdminApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        try {
            FileInputStream refreshToken = new FileInputStream("firebase.json");
            var credentials = GoogleCredentials.fromStream(refreshToken);
            var options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://aprende-palabras-b61c4.firebaseio.com")
                    .build();
            var app = FirebaseApp.initializeApp(options);
            var db = FirestoreClient.getFirestore(app);

            var wordsCountQuery = db.collection("words").count();
            var wordsCount = wordsCountQuery.get().get();
            log.info("words: {}", wordsCount.getCount());

        } catch (Throwable e) {
            log.catching(e);
        }
        context.stop();
    }
}