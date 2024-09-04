package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warranty.api.model.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
