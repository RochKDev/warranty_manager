package warranty.api.model.dto;

import java.time.LocalDate;
import java.util.List;

public record ProofOfPurchaseDto (

    String shopName,

    String reference,

    LocalDate buyDate,

    LocalDate warrantyEndDate,

//    Blob receiptImage,

    String description,

    List<ProductDto> products
){}
