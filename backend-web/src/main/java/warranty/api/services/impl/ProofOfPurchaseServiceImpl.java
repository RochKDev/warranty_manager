package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseId;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.services.ProofOfPurchaseService;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProofOfPurchaseServiceImpl implements ProofOfPurchaseService {
    private final ProofOfPurchaseRepository proofOfPurchaseRepository;

    public ProofOfPurchaseServiceImpl(final ProofOfPurchaseRepository proofOfPurchaseRepository){
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
    }

    @Override
    public ProofOfPurchase save(final ProofOfPurchase proofOfPurchase) {
        log.info("Saving proof of purchase with id {}", proofOfPurchase.getProofOfPurchaseId());
        return proofOfPurchaseRepository.save(proofOfPurchase);
    }

    @Override
    public Optional<ProofOfPurchase> findOne(final ProofOfPurchaseId proofOfPurchaseId) {
        log.info("Finding proof of purchase with id {}", proofOfPurchaseId);
        return proofOfPurchaseRepository.findById(proofOfPurchaseId);
    }

    @Override
    public List<ProofOfPurchase> findAll(final Long userId) {
        log.info("Finding all proof of purchases for user with id {}", userId);
        return proofOfPurchaseRepository.findByUser_Id(userId);
    }
    @Override
    public void deleteById(final ProofOfPurchaseId proofOfPurchaseId){
        log.info("Deleting proof of purchase with id {}", proofOfPurchaseId);
        proofOfPurchaseRepository.deleteById(proofOfPurchaseId);
    }
}

