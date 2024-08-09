package warranty.api.model.responses;

import warranty.api.model.ProofOfPurchase;

import java.time.LocalDate;
import java.util.List;

public record ProofOfPurchaseResponseDto(
        Long id,
        String shopName,
        String reference,
        LocalDate buyDate,
        LocalDate warrantyEndDate,
        String description,
        List<ProductResponseDto> products
) {
    public static ProofOfPurchaseResponseDto fromEntity(ProofOfPurchase proofOfPurchase) {
        // Convert the list of products to ProductResponseDto
        List<ProductResponseDto> productDtos = proofOfPurchase.getProducts().stream()
                .map(ProductResponseDto::fromEntity)
                .toList();

        // Return a new ProofOfPurchaseResponseDto with all fields populated
        return new ProofOfPurchaseResponseDto(
                proofOfPurchase.getId(),
                proofOfPurchase.getShopName(),
                proofOfPurchase.getReference(),
                proofOfPurchase.getBuyDate(),
                proofOfPurchase.getWarrantyEndDate(),
                proofOfPurchase.getDescription(),
                productDtos
        );
    }
}

