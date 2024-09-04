package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import warranty.api.services.impl.ImageServiceImpl;

@RestController
@RequestMapping(path = "api/v1/images")
public class ImageController {

    private ImageServiceImpl imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String uploadImage = imageService.uploadImage(file);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> downloadImage(@PathVariable("name") String name) {
        byte[] downloadImage = imageService.downloadImage(name);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(downloadImage);
    }
}
