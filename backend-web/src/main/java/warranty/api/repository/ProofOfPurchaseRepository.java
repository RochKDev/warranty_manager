package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.ProofOfPurchase;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProofOfPurchaseRepository extends JpaRepository<ProofOfPurchase, Long> {

    List<ProofOfPurchase> findByUser_Id(Long userId);

    // find a proof of purchase by shop name and reference and user id  (unique constraint)
    Optional<ProofOfPurchase> findByShopNameAndReferenceAndUser_Id(String shopName, String reference, Long userId);
}
