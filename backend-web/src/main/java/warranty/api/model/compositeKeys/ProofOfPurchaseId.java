package warranty.api.model.compositeKeys;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProofOfPurchaseId implements Serializable {

    private String shopName;

    private String reference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofOfPurchaseId that = (ProofOfPurchaseId) o;
        return Objects.equals(shopName, that.shopName) && Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopName, reference);
    }
}
