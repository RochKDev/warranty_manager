package warranty.api.model.dto;


import lombok.Data;
import warranty.api.model.compositeKeys.ProofOfPurchaseId;

// Convert to record
public record ProductDto(String name, String description, ProofOfPurchaseId proofOfPurchaseId) {
}
