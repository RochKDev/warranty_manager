package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.ProofOfPurchase;

import java.util.Optional;

@Repository
public interface ProofOfPurchaseRepository extends JpaRepository<ProofOfPurchase, Long> {

//    List<ProofOfPurchase> findByUser_Id(Long userId);

    Optional<ProofOfPurchase> findOneByShopNameAndReference(String shopName, String reference);

}
