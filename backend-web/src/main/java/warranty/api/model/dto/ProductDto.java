package warranty.api.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This class contains the product data.
 */
@Data
@AllArgsConstructor
@Builder
public class ProductDto {

    private String name;

    private String description;

    private Long proofOfPurchaseId;

    /**
     * Constructor for the ProductDto class.
     *
     * @param name        The name of the product.
     * @param description The description of the product.
     */
    public ProductDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
