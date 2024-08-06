package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.compositeKeys.ProofOfPurchaseId;

import java.util.List;

@Repository
public interface ProofOfPurchaseRepository extends JpaRepository<ProofOfPurchase, ProofOfPurchaseId> {

    List<ProofOfPurchase> findByUser_Id(Long userId);
}
