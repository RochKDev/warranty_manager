package warranty.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the proof of purchase entity in the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        // make the shopName and reference unique for each user
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"shopName", "reference", "user_id"})
        }
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

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "proofOfPurchase", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

}
