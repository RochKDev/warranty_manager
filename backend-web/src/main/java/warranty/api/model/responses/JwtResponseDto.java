package warranty.api.model.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponseDto {

    private String token;

    private String type = "Bearer";

    public JwtResponseDto(String token) {
        this.token = token;
    }
}
