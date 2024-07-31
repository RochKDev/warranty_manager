package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warranty.api.model.Warranty;
import java.util.List;

public interface WarrantyRepository extends JpaRepository<Warranty, Long> {

    List<Warranty> findByUser(Long userId);
}
