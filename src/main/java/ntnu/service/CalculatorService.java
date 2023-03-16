package ntnu.service;

import lombok.RequiredArgsConstructor;
import ntnu.models.Equation;
import ntnu.repository.EquationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private double answer;
    private Equation equation;
    private ArrayList<String>  log = new ArrayList<>();

    private final EquationRepository equationRepository;

    public void saveCalculation(Equation equation, String username) {
        solve(equation);
        equation.setResult(answer);
        equation.setUsername(username);
        equationRepository.save(equation);
    }

    public void solve(Equation equation){
        this.equation = equation;
        System.out.println(equation.getOperator());
        if(equation.getOperator() == '+'){
            answer = equation.getFactor1() + equation.getFactor2();
            return;
        }else if(equation.getOperator() == '-'){
            answer = equation.getFactor1() - equation.getFactor2();
            return;
        }else if(equation.getOperator() == '*'){
            answer = equation.getFactor1() * equation.getFactor2();
            return;
        }else if(equation.getOperator() == '/'){
            answer = equation.getFactor1() / equation.getFactor2();
            return;
        }
        answer = 0;
    }

    public double getAnswer() {
        return answer;
    }

    public String toString(){
        return equation.toString() + " = " + answer;
    }

    public boolean addToLog(String toAdd){
        if(log.size() == 0 || !toAdd.equals(log.get(log.size()-1))){
            log.add(toAdd);
            return true;
        }
        return false;
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public List<Equation> findAllCalculationsByUsername(String username) {
        return equationRepository.findAllByUsername(username);
    }

}