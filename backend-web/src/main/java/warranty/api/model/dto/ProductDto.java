package warranty.api.model.dto;


// Convert to record
public record ProductDto(
        String name,
        String description,
        Long proofOfPurchaseId
        )
{ }
