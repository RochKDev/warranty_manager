package warranty.api.model.responses;

import warranty.api.model.Product;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        Long proofOfPurchaseId
) {
    public static ProductResponseDto fromEntity(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getProofOfPurchase() != null ? product.getProofOfPurchase().getId() : null
        );
    }
}
