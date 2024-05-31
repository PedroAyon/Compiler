package generation_schemes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class QuadrupleGenerator {
    private ArrayList<Quadruple> quadruples;
    private int tempVarCount = 0;
    String expression;
    boolean isAMathExpression = true;

    public QuadrupleGenerator(String expression, int tempVarCount) {
        this.expression = expression;
        this.tempVarCount = tempVarCount;
        this.quadruples = new ArrayList<>();
        ArrayList<String> tokens = splitMathExpression(this.expression);
        generateQuadruples(tokens);
    }

    public QuadrupleGenerator(String expression, int tempVarCount, boolean isAMathExpression) {
        this.expression = expression;
        this.tempVarCount = tempVarCount;
        this.isAMathExpression = isAMathExpression;
        this.quadruples = new ArrayList<>();
        ArrayList<String> tokens = splitMathExpression(this.expression);
        generateQuadruples(tokens);
    }

    private String generateQuadruples(ArrayList<String> tokens) {
        // take care of parenthesis
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("(")) {
                int count = 1;
                int j = i;
                while (count != 0) {
                    j++;
                    if (tokens.get(j).equals(")")) {
                        count--;
                    } else if (tokens.get(j).equals("(")) {
                        count++;
                    }
                }
                var subExpression = new ArrayList<>(tokens.subList(i + 1, j));
                var left = new ArrayList<>(tokens.subList(0, i));
                var right = new ArrayList<>(tokens.subList(j + 1, tokens.size()));
                left.add(generateQuadruples(subExpression));
                left.addAll(right);
                tokens = left;
            }
        }

        if (isAMathExpression) {
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("=")) {
                    return evaluateOperation(tokens, i, token);
                }
            }

            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("+") || token.equals("-")) {
                    return evaluateOperation(tokens, i, token);
                }
            }

            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("*") || token.equals("/") || token.equals("%")) {
                    return evaluateOperation(tokens, i, token);
                }
            }

            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("^")) {
                    return evaluateOperation(tokens, i, token);
                }
            }
        } else {
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("=")) {
                    return evaluateOperation(tokens, i, token);
                }
            }
            // OR opertor
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("||")) {
                    return evaluateOperation(tokens, i, token);
                }
            }
            // AND operator
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("&&")) {
                    return evaluateOperation(tokens, i, token);
                }
            }
            // < <= > >= == !=
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("<") || token.equals("<=") || token.equals(">") || token.equals(">=") || token.equals("==") || token.equals("!=")) {
                    return evaluateOperation(tokens, i, token);
                }
            }

            // NOT operator
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String token = tokens.get(i);
                if (token.equals("!")) {
                    return evaluateOperation(tokens, i, token);
                }
            }

        }

        // expression should be a single element
        return tokens.get(0);
    }

    private String evaluateOperation(ArrayList<String> tokens, int operator_index, String operator) {
        if (Objects.equals(operator, "!")) {
            var right = new ArrayList<>(tokens.subList(operator_index + 1, tokens.size()));
            var operand1 = generateQuadruples(right);
            return applyOperation(operator, operand1, null);
        }
        var left = new ArrayList<>(tokens.subList(0, operator_index));
        var right = new ArrayList<>(tokens.subList(operator_index + 1, tokens.size()));
        var operand1 = generateQuadruples(left);
        var operand2 = generateQuadruples(right);
        return applyOperation(operator, operand1, operand2);
    }

    private String applyOperation(String op, String operand1, String operand2) {
        tempVarCount++;
        var tempVar = "t" + (tempVarCount);
        if (op.equals("=")) {
            tempVar = null;
        }
        Quadruple quadruple = new Quadruple(op, operand1, operand2, tempVar);
        quadruples.add(quadruple);
        return quadruple.resultIdentifier();

    }

    private ArrayList<String> splitMathExpression(String expr) {
        String[] tokens = expr.split(" ");
        return new ArrayList<>(Arrays.asList(tokens));
    }

    public ArrayList<Quadruple> getQuadruples() {
        return quadruples;
    }
}