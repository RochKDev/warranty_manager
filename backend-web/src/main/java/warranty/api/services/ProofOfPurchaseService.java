package warranty.api.services;

import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;

import java.util.List;

public interface ProofOfPurchaseService {
    ProofOfPurchaseResponseDto save(final ProofOfPurchaseDto proofOfPurchaseDto);
    List<ProofOfPurchaseResponseDto> findAll();
    ProofOfPurchaseResponseDto findOneById(final Long id);
    ProofOfPurchaseResponseDto findOneByShopNameAndReference(final String shopName, final String reference);
//    List<ProofOfPurchase> findAll(final Long userId);
    void deleteById(final Long id);
    ProofOfPurchaseResponseDto update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto);
}
