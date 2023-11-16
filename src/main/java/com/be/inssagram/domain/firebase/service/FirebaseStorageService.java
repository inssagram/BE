package com.be.inssagram.domain.firebase.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageService {

    private final String bucketName = System.getenv("SPRING_APP_FIREBASE_BUCKET");
    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            String firebaseKey = System.getenv("FIREBASE_KEY");
            if (firebaseKey != null && !firebaseKey.isEmpty()) {
                // Create a GoogleCredentials object from the environment variable
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(firebaseKey.getBytes(
                                StandardCharsets.UTF_8))
                );

                // Build the Storage service using the credentials
                storage = StorageOptions.newBuilder()
                        .setCredentials(credentials)
                        .setProjectId(bucketName.split("\\.")[0])
                        .build()
                        .getService();
            } else {
                System.err.println(
                        "FIREBASE_KEY environment variable is not set.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            // BlobId 생성
            BlobId blobId = BlobId.of(
                    bucketName, fileName);

            // Blob 객체 가져오기
            Blob blob = storage.get(blobId);

            if (blob != null) {
                // 파일이 존재함
                boolean deleted = storage.delete(blobId);

                if (deleted) {
                    System.out.println("파일이 성공적으로 삭제되었습니다");
                    return true;
                } else {
                    System.out.println("파일 삭제 실패");
                }
            } else {
                System.out.println("삭제할 파일이 존재하지 않습니다.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

        //            StorageClient storage1 = StorageClient.getInstance();
//            Iterable<Blob> blobs = storage1.bucket(bucketName).list().iterateAll();
//
//            for (Blob blob : blobs) {
//                // Check metadata condition (customize this based on your requirements)
//                String metadataValue = blob.getMetadata().get("your-metadata-key");
//
//                if ("desired-value".equals(metadataValue)) {
//                    // Delete the blob that matches the metadata condition
//                    blob.delete();
//                    System.out.println("Blob deleted: " + blob.getName());
//                }
//            }
//

    }

}