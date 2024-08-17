package warranty.api.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {

        private String name;

        private String description;

        private Long proofOfPurchaseId;

        public ProductDto(String name, String description) {
            this.name = name;
            this.description = description;
        }

}
