package warranty.api.services;

import org.springframework.security.core.userdetails.UserDetails;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;

import java.util.List;

public interface ProofOfPurchaseService {

    ProofOfPurchaseResponseDto save(final ProofOfPurchaseDto proofOfPurchaseDto, UserDetails userDetails);

    List<ProofOfPurchaseResponseDto> findAll(UserDetails userDetails);

    ProofOfPurchaseResponseDto findOneById(final Long id, UserDetails userDetails);

    ProofOfPurchaseResponseDto findOneByShopNameAndReference(final String shopName, final String reference,
                                                             UserDetails userDetails);
    void deleteById(final Long id, UserDetails userDetails);

    ProofOfPurchaseResponseDto update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto, UserDetails userDetails);
}
