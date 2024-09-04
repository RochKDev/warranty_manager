package warranty.api.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file);

    byte[] downloadImage(String name);
}
