package ntnu.repository;

import ntnu.models.Equation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquationRepository extends JpaRepository<Equation, Long> {
    List<Equation> findAllByUsername(String username);
}
