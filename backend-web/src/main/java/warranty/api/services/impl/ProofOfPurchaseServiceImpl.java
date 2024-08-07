package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProofOfPurchaseConflictException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.services.ProofOfPurchaseService;

import java.time.LocalDate;
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
    public ProofOfPurchase save(final ProofOfPurchaseDto proofOfPurchaseDto) {

        // Check if shopName and same reference exists
        Optional<ProofOfPurchase> existingProofOfPurchase =
                proofOfPurchaseRepository.findOneByShopNameAndReference(proofOfPurchaseDto.shopName(),
                        proofOfPurchaseDto.reference());

        if(existingProofOfPurchase.isPresent()){
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
        // Return the saved ProofOfPurchase entity (and cascade the save to the associated Product entities)
        return proofOfPurchaseRepository.save(proofOfPurchase);
    }

    @Override
    public ProofOfPurchase findOneById(final Long id) {
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findById(id);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + id + " not found");
        }

        log.info("Finding proof of purchase with id {}", id);
        return proofOfPurchase.get();
    }

    @Override
    public ProofOfPurchase findOneByShopNameAndReference(String shopName, String reference) {
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findOneByShopNameAndReference(shopName, reference);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with shop name " + shopName + " and reference " + reference + " not found");
        }

        log.info("Finding proof of purchase with shop name {} and reference {}", shopName, reference);
        return proofOfPurchase.get();
    }


    //    @Override
//    public List<ProofOfPurchase> findAll(final Long userId) {
//        log.info("Finding all proof of purchases for user with id {}", userId);
//        return proofOfPurchaseRepository.findByUser_Id(userId);
//    }
    @Override
    public void deleteById(final Long id){
        log.info("Deleting proof of purchase with id {}", id);
        proofOfPurchaseRepository.deleteById(id);
    }
    @Override
    public ProofOfPurchase update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto){

        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findById(id);

        if(proofOfPurchase.isEmpty()){
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + id + " not found");
        }

        ProofOfPurchase existingProofOfPurchase = getProofOfPurchase(proofOfPurchaseDto, proofOfPurchase);

        log.info("Updating proof of purchase with id {}", id);
        return proofOfPurchaseRepository.save(existingProofOfPurchase);
    }

    // TODO add case specific update (eg. update description). Partial update.

    private static ProofOfPurchase getProofOfPurchase(ProofOfPurchaseDto proofOfPurchaseDto, Optional<ProofOfPurchase> proofOfPurchase) {
        ProofOfPurchase existingProofOfPurchase = proofOfPurchase.get();

        existingProofOfPurchase.setBuyDate(proofOfPurchaseDto.buyDate());
        existingProofOfPurchase.setDescription(proofOfPurchaseDto.description());
        //existingProofOfPurchase.setReceiptImage(proofOfPurchaseDto.receiptImage());
        existingProofOfPurchase.setWarrantyEndDate(proofOfPurchaseDto.warrantyEndDate());
        existingProofOfPurchase.setShopName(proofOfPurchaseDto.shopName());
        existingProofOfPurchase.setReference(proofOfPurchaseDto.reference());
        return existingProofOfPurchase;
    }

}

