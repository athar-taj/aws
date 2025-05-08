package com.aws.service.s3bucket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;
    @Autowired
    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file){
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build(), convertMultipartFileToFile(file).toPath());
        return fileName;
    }

    public String generateObjectUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();

        S3Presigner preSigner = S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();

        String presignedUrl = preSigner.presignGetObject(presignRequest).url().toString();
        preSigner.close();

        return presignedUrl;
    }

    public byte[] downloadFile(String key){
        try {
            return s3Client.getObjectAsBytes(b -> b.bucket(bucketName).key(key)).asByteArray();
        }
        catch (Exception e){
            log.error("Error While Downloading the File  " +e);
        }
        return null;
    }

    private File convertMultipartFileToFile(MultipartFile file){
        File convFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error While Converting the File  " +e);
        }
        return convFile;
    }
}
