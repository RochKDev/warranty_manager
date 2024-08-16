package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProofOfPurchaseConflictException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.exception.UnauthorizedResourceAccess;
import warranty.api.exception.UserEmailNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.User;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.repository.UserRepository;
import warranty.api.services.ProofOfPurchaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ProofOfPurchaseServiceImpl implements ProofOfPurchaseService {

    private final ProofOfPurchaseRepository proofOfPurchaseRepository;

    private final UserRepository userRepository;

    public ProofOfPurchaseServiceImpl(final ProofOfPurchaseRepository proofOfPurchaseRepository, UserRepository userRepository) {
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
        this.userRepository = userRepository;

    }

    @Override
    public ProofOfPurchaseResponseDto save(final ProofOfPurchaseDto proofOfPurchaseDto, UserDetails userDetails) {
        User existingUser = getUserFromUserDetails(userDetails);
        // Get eventual proof of purchase with same shop name and reference
        Optional<ProofOfPurchase> existingProofOfPurchase =
                proofOfPurchaseRepository.findByShopNameAndReferenceAndUser_Id(proofOfPurchaseDto.shopName(),
                        proofOfPurchaseDto.reference(), existingUser.getId());
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
                .user(existingUser)
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
    public Page<ProofOfPurchaseResponseDto> findAll(Pageable pageable, UserDetails userDetails) {
        User existingUser = getUserFromUserDetails(userDetails);

        log.info("Finding all proof of purchases related to user : {}", existingUser.getEmail());

        return proofOfPurchaseRepository.findByUser_Id(existingUser.getId(), pageable)
                .map(ProofOfPurchaseResponseDto::fromEntity);
    }

    @Override
    public ProofOfPurchaseResponseDto findOneById(final Long id, UserDetails userDetails) {
        ProofOfPurchase proofOfPurchase = getProofOfPurchase(id);
        User existingUser = getUserFromUserDetails(userDetails);

        checkAuthorizedUser(existingUser, proofOfPurchase);

        log.info("Finding proof of purchase with id {}", id);
        return ProofOfPurchaseResponseDto.fromEntity(proofOfPurchase);
    }

    @Override
    public ProofOfPurchaseResponseDto findOneByShopNameAndReference(String shopName, String reference, UserDetails userDetails) {
        User existingUser = getUserFromUserDetails(userDetails);

        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findByShopNameAndReferenceAndUser_Id(
                shopName,
                reference,
                existingUser.getId()
        );

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with shop name " + shopName + " and reference " + reference + " not found");
        }

        checkAuthorizedUser(existingUser, proofOfPurchase.get());

        log.info("Finding proof of purchase with shop name {} and reference {}", shopName, reference);
        return ProofOfPurchaseResponseDto.fromEntity(proofOfPurchase.get());
    }

    @Override
    public void deleteById(final Long id, UserDetails userDetails) {

        ProofOfPurchase proofOfPurchase = getProofOfPurchase(id);

        User existingUser = getUserFromUserDetails(userDetails);
        checkAuthorizedUser(existingUser, proofOfPurchase);
        log.info("Deleting proof of purchase with id {}", id);
        proofOfPurchaseRepository.deleteById(id);
    }

    @Override
    public ProofOfPurchaseResponseDto update(final Long id, final ProofOfPurchaseDto proofOfPurchaseDto,
                                             UserDetails userDetails) {

        User existingUser = getUserFromUserDetails(userDetails);

        ProofOfPurchase proofOfPurchase = getProofOfPurchase(id);
        checkAuthorizedUser(existingUser, proofOfPurchase);

        ProofOfPurchase updatedProofOfPurchase = updateProofOfPurchase(proofOfPurchaseDto, proofOfPurchase);

        log.info("Updating proof of purchase with id {}", id);

        // Save and convert to response DTO and return
        return ProofOfPurchaseResponseDto.fromEntity(proofOfPurchaseRepository.save(updatedProofOfPurchase));
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


    private User getUserFromUserDetails(UserDetails userDetails) {
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new UserEmailNotFoundException("User with email " + userDetails.getUsername() + " not found");
        }

        return user.get();
    }

    private void checkAuthorizedUser(User user, ProofOfPurchase proofOfPurchase) {
        if(!Objects.equals(proofOfPurchase.getUser().getId(), user.getId())) {
            throw new UnauthorizedResourceAccess("You are not authorized to access this proof of purchase.");
        }
    }

    private ProofOfPurchase getProofOfPurchase(Long proofOfPurchaseId) {
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findById(proofOfPurchaseId);

        if (proofOfPurchase.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + proofOfPurchaseId + " not found");
        }

        return proofOfPurchase.get();
    }
}
