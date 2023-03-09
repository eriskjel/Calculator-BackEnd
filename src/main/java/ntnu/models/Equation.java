package ntnu.models;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calculation")
public class Equation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculationId", unique = true, nullable = false)
    private int id;

    @Column(name = "factor1")
    private double factor1;

    @Column(name = "factor2")
    private double factor2;

    @Column(name = "operator")
    private char operator;

}
