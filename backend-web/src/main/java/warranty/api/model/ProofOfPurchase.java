package warranty.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"shopName", "reference"})
)
public class ProofOfPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;

    private String reference;

    private LocalDate buyDate;

    private LocalDate warrantyEndDate;

//    @Lob
//    private Blob receiptImage; // TODO implement file upload

    private String description;

//    @ManyToOne
//    User user; // TODO implement authentication

    @OneToMany(mappedBy = "proofOfPurchase", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

}
