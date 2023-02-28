package ntnu.controllers;

import ntnu.services.CalculatorService;
import ntnu.models.Equation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/calc")
public class CalculatorController {

    @Autowired
    private CalculatorService service;

    Logger logger = LoggerFactory.getLogger(CalculatorController.class);


    @GetMapping("/ans")
    public double answer(){
        logger.info("Retrieved answer: " + service.getAnswer());
        return service.getAnswer();
    }

    @PostMapping("/solve")
    public double solve(@RequestBody Equation equation){
        service.solve(equation);
        logger.info("Equation: n1: " + equation.getN1() +", n2: " +  equation.getN2()
                + ", operator: " + equation.getOperator());
        logger.info("Answer: " + service.getAnswer());

        if(service.addToLog(service.toString())){
            logger.info("Added to log: " + service.toString());
        }
        return service.getAnswer();
    }

    @GetMapping("/log")
    public ArrayList<String> log(){
        logger.info("Returned log: " + service.toString());
        return service.getLog();
    }

}
