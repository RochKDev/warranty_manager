package warranty.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.Product;

/**
 * This interface is responsible for managing the product data.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Find all products by proof of purchase user id.
     *
     * @param userId   The id of the user.
     * @param pageable The pageable object.
     * @return The page of products.
     */
    Page<Product> findByProofOfPurchase_User_Id(Long userId, Pageable pageable);
}
