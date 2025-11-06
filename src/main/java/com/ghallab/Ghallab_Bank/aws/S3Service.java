package com.ghallab.Ghallab_Bank.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID() + fileExtension;
        String s3Key = folderName + "/" + newFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3Key)).toString();
    }

    public Boolean deleteFile(String fileUrl) {
        try {
            // Extract the full key from the URL (including folder)
            String key = extractKeyFromUrl(fileUrl);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (S3Exception e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            return false;
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        // The URL format is: https://storage.yandexcloud.net/bucket-name/profile-pictures/uuid.jpg
        // We need to extract: profile-pictures/uuid.jpg
        try {
            URI uri = URI.create(fileUrl);
            String path = uri.getPath();
            // Remove the leading slash and bucket name part
            if (path.startsWith("/" + bucketName + "/")) {
                return path.substring(bucketName.length() + 2); // +2 for the two slashes
            }
            return path.substring(1); // Remove leading slash
        } catch (Exception e) {
            // Fallback: if URL parsing fails, try to extract the filename
            log.warn("Failed to parse S3 URL, using fallback extraction");
            return "profile-pictures/" + fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }
    }
}