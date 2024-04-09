package com.github.bibenga.palabras.firebase;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ImportUserRecord;
import com.google.firebase.auth.UserImportHash;
import com.google.firebase.auth.UserImportOptions;
import com.google.firebase.auth.hash.Pbkdf2Sha256;
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
            var appOptions = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://aprende-palabras-b61c4.firebaseio.com")
                    .build();
            var app = FirebaseApp.initializeApp(appOptions);
            var db = FirestoreClient.getFirestore(app);
            var auth = FirebaseAuth.getInstance(app);

            // var wordsCountQuery = db.collection("words").count();
            // var wordsCount = wordsCountQuery.get().get();
            // log.info("words: {}", wordsCount.getCount());

            // var claims = new HashMap<String, Object>();
            // claims.put("role", "USER");
            // auth.setCustomUserClaims("uAmDqWLNUNNj8NKzJKbuVC7z7xf1", claims);

            var users = auth.listUsers(null);
            for (var user : users.getValues()) {
                log.info("uid={}, email={}, isDisabled={}, claims={}",
                        user.getUid(), user.getEmail(), user.isDisabled(), user.getCustomClaims());
            }

            // <algorithm>$<iterations>$<salt>$<hash>
            // pbkdf2_sha256$600000$8RpnZDpB97fanEAGY6cLNo$VTYAKQ2wVKg9eaGQVmGf6wlmPdO2QrEglxxVxWMbD5o=
            // var options = UserImportOptions.withHash(
            // Pbkdf2Sha256.builder()
            // .setRounds(600000)
            // .build());
            // var options = UserImportOptions.withHash(new MyPbkdf2Sha256(600000));
            // var djangoUsers = new ArrayList<ImportUserRecord>();
            // djangoUsers.add(ImportUserRecord.builder()
            // .setUid("django-a")
            // .setEmail("a-django-13234234@gmail.com")
            // .setPasswordHash(BaseEncoding.base64().decode("VTYAKQ2wVKg9eaGQVmGf6wlmPdO2QrEglxxVxWMbD5o="))
            // .setPasswordSalt(BaseEncoding.base64().decode("8RpnZDpB97fanEAGY6cLNo"))
            // .build());
            // var importRes = auth.importUsers(djangoUsers, options);
            // log.error("import result: succes={}, failure={}",
            // importRes.getSuccessCount(), importRes.getFailureCount());
            // for (var err : importRes.getErrors()) {
            // log.error("{}: {}", err.getIndex(), err.getReason());
            // }

        } catch (Throwable e) {
            log.catching(e);
        }
        context.stop();
    }

    static class MyPbkdf2Sha256 extends UserImportHash {

        private final int rounds;

        public MyPbkdf2Sha256(int rounds) {
            super("PBKDF2_SHA256");
            this.rounds = rounds;
        }

        @Override
        protected Map<String, Object> getOptions() {
            return ImmutableMap.<String, Object>of("rounds", rounds);
        }
    }
}