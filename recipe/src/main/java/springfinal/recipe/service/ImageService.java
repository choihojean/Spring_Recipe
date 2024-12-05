package springfinal.recipe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    public String saveImage(MultipartFile image) throws IOException {
        String path = System.getProperty("user.dir") + "/uploads/images/";
        String uuidImageName = UUID.randomUUID().toString() + "." + getImageExtension(Objects.requireNonNull(image.getOriginalFilename()));
        String dir = path + uuidImageName;

        File destination = new File(dir);

        image.transferTo(destination);
        return "/uploads/images/" + uuidImageName;
    }

    private String getImageExtension(String imageName) {
        int lastDotIndex = imageName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return "";
        }

        return imageName.substring(lastDotIndex + 1);
    }
}
