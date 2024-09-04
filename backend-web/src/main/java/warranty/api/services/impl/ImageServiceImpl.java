package warranty.api.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import warranty.api.model.Image;
import warranty.api.repository.ImageRepository;
import warranty.api.utils.ImageUtils;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String uploadImage(MultipartFile file) {
        Image image = null;
        try {
            image = imageRepository.save(Image.builder()
                   .name(file.getOriginalFilename())
                   .type(file.getContentType())
                   .data(ImageUtils.compressImage(file.getBytes()))
                   .build());
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO treat this exception correctly
        }
        if(image != null) {
             return "Image uploaded successfully" + file.getOriginalFilename();
         }
         return null;
    }
    public byte[] downloadImage(String name) {
        Optional<Image> image = imageRepository.findByName(name);
        return image.map(value -> ImageUtils.decompressImage(value.getData())).orElse(null);
    }

}
