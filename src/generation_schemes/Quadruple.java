package generation_schemes;

public record Quadruple(
        String operator,
        String operand1,
        String operand2,
        String resultIdentifier
) {
    public String toString() {
        if (operator.equals("!")) {
            return resultIdentifier + " = " + operator + operand1;
        }
        if (operator.equals("=")) {
            return operand1 + " = " + operand2;
        }
        return resultIdentifier + " = " + operand1 + " " + operator + " " + operand2;
    }
}
