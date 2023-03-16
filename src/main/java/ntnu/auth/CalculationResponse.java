package ntnu.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.enums.AuthenticationState;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResponse {
    private double result;
    private String errorMessage;
    private AuthenticationState authenticationState;
}
