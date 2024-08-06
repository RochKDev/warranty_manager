package warranty.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import warranty.api.model.dto.ProofOfPurchaseId;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofOfPurchase {

    @EmbeddedId
    ProofOfPurchaseId proofOfPurchaseId;

    private LocalDate buyDate;

    private LocalDate warrantyEndDate;

    @Lob
    private Blob receiptImage;

    private String description;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "proofOfPurchase", cascade = CascadeType.ALL)
    private List<Product> products;

}
