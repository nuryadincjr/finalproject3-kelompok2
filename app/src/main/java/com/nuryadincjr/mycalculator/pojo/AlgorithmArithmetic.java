package com.nuryadincjr.mycalculator.pojo;

import static com.nuryadincjr.mycalculator.pojo.Constants.*;
import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;

import java.util.List;
import java.util.Stack;

public class AlgorithmArithmetic {

    public static double getExpressions(List<String> expressionsList){
        Stack<String> operatorsStack = new Stack<>();
        Stack<Double> operandsStack = new Stack<>();

        for (String expression: expressionsList) {
            if(OPERATORS_PATTERN.contains(expression)){
                operatorsStack.push(expression);
            } else operandsStack.push(Double.parseDouble(expression));

            onPreparedPriority(operandsStack, operatorsStack);
        }
        return operandsStack.pop();
    }

    private static int getOperatorPriority(String operator) {
        switch (operator) {
            case "%":
                return 0;
            case "*":
            case "/":
                return 1;
            case "+":
            case "-":
                return 2;
            case "=":
                return 3;
            default:
                return 4;
        }
    }

    private static void onPreparedPriority(Stack<Double> operandsStack,
                                           Stack<String> operatorsStack) {
        while(operatorsStack.size() >=2) {
            String firstOperator = valueOf(operatorsStack.pop());
            String secondOperator = valueOf(operatorsStack.pop());

            if(!firstOperator.equals("=") && secondOperator.equals("%")) {
                String firstOperand = valueOf(operandsStack.pop());
                String secondOperand = valueOf(0);

                operandsStack.push(getResults(parseDouble(firstOperand),
                        parseDouble(secondOperand), firstOperator, secondOperator));
                operatorsStack.push(firstOperator);
            } else {
                if(getOperatorPriority(firstOperator) < getOperatorPriority(secondOperator)) {
                    operatorsStack.push(secondOperator);
                    operatorsStack.push(firstOperator);
                    return;
                } else {
                    String firstOperand = valueOf(operandsStack.pop());
                    String secondOperand;

                    if(!firstOperator.equals("=") && secondOperator.equals("-")) {
                        secondOperand = valueOf(0);
                    }else secondOperand = valueOf(operandsStack.pop());

                    operandsStack.push(getResults(parseDouble(firstOperand),
                            parseDouble(secondOperand), firstOperator, secondOperator));
                    operatorsStack.push(firstOperator);
                }
            }
        }
    }

    private static double getResults(double firstOperand, double secondOperand,
                                     String firstOperator, String secondOperator) {
        switch (secondOperator) {
            case "%":
                if(!firstOperator.equals("=")) {
                    return (firstOperand /100);
                }else return ((secondOperand * firstOperand) / 100);
            case "*":
                return (secondOperand * firstOperand);
            case "/":
                return (secondOperand / firstOperand);
            case "+":
                return (secondOperand + firstOperand);
            case "-":
                return (secondOperand - firstOperand);
            default:
                return 0;
        }
    }
}
