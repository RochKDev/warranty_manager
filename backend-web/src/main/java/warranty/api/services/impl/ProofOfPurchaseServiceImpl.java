package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProofOfPurchaseConflictException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.services.ProofOfPurchaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProofOfPurchaseServiceImpl implements ProofOfPurchaseService {
    private final ProofOfPurchaseRepository proofOfPurchaseRepository;

    public ProofOfPurchaseServiceImpl(final ProofOfPurchaseRepository proofOfPurchaseRepository) {
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
    }

    @Override
    public ProofOfPurchaseResponseDto save(final ProofOfPurchaseDto proofOfPurchaseDto) {
        // Get eventual proof of purchase with same shop name and reference
        Optional<ProofOfPurchase> existingProofOfPurchase =
                proofOfPurchaseRepository.findOneByShopNameAndReference(proofOfPurchaseDto.shopName(),
                        proofOfPurchaseDto.reference());
        // Check if a proof of purchase with the same shop name and reference already exists
        if (existingProofOfPurchase.isPresent()) {
            throw new ProofOfPurchaseConflictException("Proof of purchase with shop name " + proofOfPurchaseDto.shopName() +
                    " and reference " + proofOfPurchaseDto.reference() + " already exists");
        }

        // Calculate the warranty end date
        LocalDate warrantyEndDate = proofOfPurchaseDto.buyDate().plusYears(2);

        // Create the ProofOfPurchase entity
        ProofOfPurchase proofOfPurchase = ProofOfPurchase.builder()
                .shopName(proofOfPurchaseDto.shopName())
                .reference(proofOfPurchaseDto.reference())
                .buyDate(proofOfPurchaseDto.buyDate())
                .warrantyEndDate(warrantyEndDate)
                //.receiptImage(proofOfPurchaseDto.receiptImage())
                .description(proofOfPurchaseDto.description())
                .build();

        // Convert the product DTOs to Product entities and set the proofOfPurchase for each product
        List<Product> productList = proofOfPurchaseDto.products().stream()
                .map(productDto -> Product.builder()
                        .name(productDto.name())
                        .description(productDto.description())
                        .proofOfPurchase(proofOfPurchase) // Set the proofOfPurchase here
                        .build())
                .toList();

        // Set the products in the proofOfPurchase entity
        proofOfPurchase.setProducts(productList);

        log.info("Saving new proof of purchase");

        // Save the ProofOfPurchase entity
        ProofOfPurchase savedProofOfPurchase = proofOfPurchaseRepository.save(proofOfPurchase);

        // Convert the saved entity to a ProofOfPurchaseResponseDto
        return ProofOfPurchaseResponseDto.fromEntity(savedProofOfPurchase);
    }

    @Override
    public List<ProofOfPurchaseResponseDto> findAll() {
        log.info("Finding all proof of purchases");
        return proofOfPurchaseRepository.findAll().stream()
                .map(ProofOfPurchaseResponseDto::fromEntity)
                .toList();
    }

    @Override
    public ProofOfPurchaseResponseDto findOneById(final Long id) {
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findById(id);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + id + " not found");
        }

        log.info("Finding proof of purchase with id {}", id);
        return ProofOfPurchaseResponseDto.fromEntity(proofOfPurchase.get());
    }

    @Override
    public ProofOfPurchaseResponseDto findOneByShopNameAndReference(String shopName, String reference) {
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findOneByShopNameAndReference(shopName, reference);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with shop name " + shopName + " and reference " + reference + " not found");
        }

        log.info("Finding proof of purchase with shop name {} and reference {}", shopName, reference);
        return ProofOfPurchaseResponseDto.fromEntity(proofOfPurchase.get());
    }

    @Override
    public void deleteById(final Long id) {
        log.info("Deleting proof of purchase with id {}", id);
        proofOfPurchaseRepository.deleteById(id);
    }

    @Override
    public ProofOfPurchaseResponseDto update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto) {

        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findById(id);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + id + " not found");
        }

        ProofOfPurchase existingProofOfPurchase = updateProofOfPurchase(proofOfPurchaseDto, proofOfPurchase.get());

        log.info("Updating proof of purchase with id {}", id);

        // Save the updated entity
        ProofOfPurchase updatedProofOfPurchase = proofOfPurchaseRepository.save(existingProofOfPurchase);

        // Convert to response DTO and return
        return ProofOfPurchaseResponseDto.fromEntity(updatedProofOfPurchase);
    }

    // Helper method to update the existing entity with new data from DTO
    private ProofOfPurchase updateProofOfPurchase(ProofOfPurchaseDto proofOfPurchaseDto, ProofOfPurchase proofOfPurchase) {
        proofOfPurchase.setBuyDate(proofOfPurchaseDto.buyDate());
        proofOfPurchase.setDescription(proofOfPurchaseDto.description());
        //proofOfPurchase.setReceiptImage(proofOfPurchaseDto.receiptImage());
        proofOfPurchase.setWarrantyEndDate(proofOfPurchaseDto.warrantyEndDate());
        proofOfPurchase.setShopName(proofOfPurchaseDto.shopName());
        proofOfPurchase.setReference(proofOfPurchaseDto.reference());
        return proofOfPurchase;
    }
}
