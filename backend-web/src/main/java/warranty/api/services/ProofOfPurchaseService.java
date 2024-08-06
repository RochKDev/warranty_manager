package warranty.api.services;

import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseId;

import java.util.List;
import java.util.Optional;

public interface ProofOfPurchaseService {
    ProofOfPurchase save(final ProofOfPurchase proofOfPurchase);
    Optional<ProofOfPurchase> findOne(final ProofOfPurchaseId proofOfPurchaseId);
    List<ProofOfPurchase> findAll(final Long userId);
    void deleteById(final ProofOfPurchaseId proofOfPurchaseId);
    ProofOfPurchase update(final ProofOfPurchaseId proofOfPurchaseId, final ProofOfPurchase proofOfPurchase);
}
