package warranty.api.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import warranty.api.model.Image;
import warranty.api.repository.ImageRepository;

@Service
public class ImageServiceImpl {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String uploadImage(MultipartFile file) {
        imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build());
    }

}
