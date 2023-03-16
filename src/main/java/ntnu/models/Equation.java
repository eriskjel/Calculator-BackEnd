package ntnu.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calculation")
public class Equation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id", unique = true, nullable = false)
    private int id;

    @Column(name = "factor1")
    private double factor1;

    @Column(name = "factor2")
    private double factor2;

    @Column(name = "operator")
    private char operator;

    @JsonCreator
    public Equation(
            @JsonProperty("n1") double factor1,
            @JsonProperty("n2") double factor2,
            @JsonProperty("operator") String operator) {
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.operator = operator != null && operator.length() > 0 ? operator.charAt(0) : '\0';
    }
}
