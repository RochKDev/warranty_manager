package warranty.api.services;

import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseDto;

public interface ProofOfPurchaseService {
    ProofOfPurchase save(final ProofOfPurchaseDto proofOfPurchaseDto);
    ProofOfPurchase findOneById(final Long id);
    ProofOfPurchase findOneByShopNameAndReference(final String shopName, final String reference);
//    List<ProofOfPurchase> findAll(final Long userId);
    void deleteById(final Long id);
    ProofOfPurchase update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto);
}
