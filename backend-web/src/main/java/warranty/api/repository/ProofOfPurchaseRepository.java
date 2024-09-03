package warranty.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.ProofOfPurchase;

import java.util.List;
import java.util.Optional;

/**
 * This interface is responsible for managing the proof of purchase data.
 */
@Repository
public interface ProofOfPurchaseRepository extends JpaRepository<ProofOfPurchase, Long> {

    /**
     * Find all proof of purchases by user id.
     *
     * @param userId   The id of the user.
     * @param pageable The pageable object.
     * @return The page of proof of purchases.
     */
    Page<ProofOfPurchase> findByUser_Id(Long userId, Pageable pageable);

    /**
     * Find a proof of purchase by shop name, reference and user id (unique constraint).
     *
     * @param shopName  The name of the shop.
     * @param reference The reference of the proof of purchase.
     * @param userId    The id of the user.
     * @return The optional proof of purchase.
     */
    Optional<ProofOfPurchase> findByShopNameAndReferenceAndUser_Id(String shopName, String reference, Long userId);
}
