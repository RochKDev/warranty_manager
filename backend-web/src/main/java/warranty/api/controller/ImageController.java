package warranty.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import warranty.api.services.ImageService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            log.error("Error reading file: {}", originalFilename);
            throw new RuntimeException(e);
        }
        String contentType = determineImageType(fileBytes);
        log.info("Uploaded file type: {}", contentType);
        String uploadImage = imageService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }


    @GetMapping(path = "/{name}")
    public ResponseEntity<?> downloadImage(@PathVariable("name") String name) {
        byte[] downloadImage = imageService.downloadImage(name);
        String contentType = determineImageType(downloadImage);
        log.info("Content type: {}", contentType);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .body(downloadImage);
    }

    private String determineImageType(byte[] imageBytes) {
        Tika tika = new Tika();
        return tika.detect(imageBytes);
    }
}
