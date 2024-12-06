package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import springfinal.recipe.config.S3Config;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private S3Client s3Client;

    @Autowired
    private String bucketName;

    public String saveImageToS3(MultipartFile image) throws IOException {
        String imageName = UUID.randomUUID().toString() + "." + getImageExtension(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageName)
                    .contentType(image.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
        } catch (Error e) {
            System.out.println(e);
        }
        return "https://" + bucketName + ".s3.amazonaws.com/" + imageName;
    }

    private String getImageExtension(String imageName) {
        int lastDotIndex = imageName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return "";
        }

        return imageName.substring(lastDotIndex + 1);
    }
}
