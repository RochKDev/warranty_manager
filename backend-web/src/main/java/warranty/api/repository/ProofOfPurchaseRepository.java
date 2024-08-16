package warranty.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.ProofOfPurchase;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProofOfPurchaseRepository extends JpaRepository<ProofOfPurchase, Long> {

    Page<ProofOfPurchase> findByUser_Id(Long userId, Pageable pageable);

    // find a proof of purchase by shop name and reference and user id  (unique constraint)
    Optional<ProofOfPurchase> findByShopNameAndReferenceAndUser_Id(String shopName, String reference, Long userId);
}
