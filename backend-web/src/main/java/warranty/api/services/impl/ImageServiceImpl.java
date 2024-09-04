package warranty.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import warranty.api.model.Image;
import warranty.api.repository.ImageRepository;
import warranty.api.services.ImageService;
import warranty.api.utils.ImageUtils;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {


    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
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
        return "Image uploaded successfully : " + file.getOriginalFilename();
    }

    @Override
    public byte[] downloadImage(String name) {
        Optional<Image> image = imageRepository.findByName(name);
        return image.map(value -> ImageUtils.decompressImage(value.getData())).orElse(null);
    }

}
