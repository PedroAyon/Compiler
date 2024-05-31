package generation_schemes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssemblyGenerator {
    private StringBuilder dataSection;
    private StringBuilder textSection;
    private StringBuilder code;
    private Map<String, String> dataTypes = new HashMap<>();

    public AssemblyGenerator() {
        dataSection = new StringBuilder().append("section .data\n\t");
        textSection = new StringBuilder().append("section .text\n\tglobal _start\n\t");
        code = new StringBuilder().append("_start:\n\t");
    }

    public void addDeclaration(String dataType, String identifier) {
        dataTypes.put(identifier, dataType);
        dataSection.append(identifier).append(" ").append(assemblyDataType(dataType)).append(" ").append(ValueUtils.defaultForDataType(dataType));
        if (dataType.equals("string")) dataSection.append(", 0xA");
        dataSection.append("\n\t");
    }

    public void addAssignment(String identifier, String operator, String operand1, String operand2) {
        if (Objects.equals(operand1, "true")) operand1 = "1";
        if (Objects.equals(operand1, "false")) operand1 = "0";
        boolean op1IsIdentifier = dataTypes.containsKey(operand1);
                String op1Val = op1IsIdentifier ? "[" + operand1 + "]" : operand1;
        if (operator == null) { // assignment is just a single value stored in operand1
            code.append("mov eax, ").append(op1Val).append("\n\t");
            code.append("mov [").append(identifier).append("], eax\n\t");
            return;
        }
        if (operator.equals("!")) {
            code.append("mov eax, ").append(op1Val).append("\n\t");
            code.append("not eax").append("\n\t");
            code.append("mov [").append(identifier).append("], eax").append("\n\t");
        }
        if (Objects.equals(operand2, "true")) operand2 = "1";
        if (Objects.equals(operand2, "false")) operand2 = "0";
        boolean op2IsIdentifier = dataTypes.containsKey(operand2);
        String op2Val = op2IsIdentifier ? "[" + operand2 + "]" : operand2;
        switch (operator) {
            case "+":
            case "-":
            case "*":
                String assemblyOperator = switch (operator) {
                    case "+" -> "add";
                    case "-" -> "sub";
                    case "*" -> "imul";
                    default -> null;
                };
                code.append("mov eax, ").append(op1Val).append("\n\t");
                code.append(assemblyOperator).append(" eax, ").append(op2Val).append("\n\t");
                code.append("mov [").append(identifier).append("], eax\n\t");
                break;
            case "/":
                code.append("mov eax, ").append(op1Val).append("\n\t");
                code.append("mov ebx, ").append(op2Val).append("\n\t");
                code.append("xor edx, edx").append("\n\t");
                code.append("idiv ebx").append("\n\t");
                code.append("mov [").append(identifier).append("], eax\n\t"); // cociente
                break;
            case "%":
                code.append("mov eax, ").append(op1Val).append("\n\t");
                code.append("mov ebx, ").append(op2Val).append("\n\t");
                code.append("xor edx, edx").append("\n\t");
                code.append("idiv ebx").append("\n\t");
                code.append("mov [").append(identifier).append("], edx\n\t"); // residuo
                break;
            case "&&":
                code.append("mov eax, ").append(op1Val).append("\n\t");
                code.append("and eax, ").append(op2Val).append("\n\t");
                code.append("mov [").append(identifier).append("], eax\n\t");
                break;
            case "||":
                code.append("mov eax, ").append(op1Val).append("\n\t");
                code.append("or eax, ").append(op2Val).append("\n\t");
                code.append("mov [").append(identifier).append("], eax\n\t");
                break;
        }
    }

    private String assemblyDataType(String dataType) {
        return switch (dataType) {
            case "int", "float" -> "dd";
            case "double" -> "dq";
            case "boolean", "string" -> "db";
            default -> "dd";
        };
    }

    @Override
    public String toString() {
        code.append("""
                mov rax, 60
                    mov rdi, 0
                    syscall
                """);
        return dataSection.toString() + "\n" + textSection.toString() + "\n" + code;
    }
}
