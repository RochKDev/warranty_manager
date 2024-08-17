package warranty.api.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductDto {

        private String name;

        private String description;

        private Long proofOfPurchaseId;

        public ProductDto(String name, String description) {
            this.name = name;
            this.description = description;
        }

}
