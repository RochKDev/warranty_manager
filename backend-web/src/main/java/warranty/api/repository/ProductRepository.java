package warranty.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.Product;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference(String shopName, String reference);

    Page<Product> findByProofOfPurchase_User_Id(Long userId, Pageable pageable);
}
