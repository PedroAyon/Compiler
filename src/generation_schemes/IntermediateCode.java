package generation_schemes;

import java.util.ArrayList;
import java.util.Objects;

public class IntermediateCode {
    private StringBuilder code;
    private int tempVarCount = 0;

    public IntermediateCode() {
        this.code = new StringBuilder();
    }

    public void addDeclaration(String dataType, String expression, boolean isConstant) {
        QuadrupleGenerator quadrupleGenerator = new QuadrupleGenerator(expression, tempVarCount, !Objects.equals(dataType, "bool"));
        ArrayList<Quadruple> quadruples = quadrupleGenerator.getQuadruples();
        tempVarCount += quadruples.size() - 1;
        for (Quadruple quadruple : quadruples) {
            if (quadruple.operator().equals("=")) {
                if (isConstant) code.append("const ");
                code.append(dataType).append(" ").append(quadruple.operand1()).append(";").append("\n");
            } else {
                code.append(dataType).append(" ").append(quadruple.resultIdentifier()).append(";").append("\n");
            }
        }
        for (Quadruple quadruple : quadruples) {
            code.append(quadruple.toString()).append(";\n");
        }
    }

    public void addEmptyDeclaration(String dataType, String identifier, boolean isConstant) {
        if (isConstant) code.append("const ");
        code.append(dataType).append(" ").append(identifier).append(";\n");
    }

    public void addAssignation(String dataType, String expression) {
        QuadrupleGenerator quadrupleGenerator = new QuadrupleGenerator(expression, tempVarCount, !Objects.equals(dataType, "bool"));
        ArrayList<Quadruple> quadruples = quadrupleGenerator.getQuadruples();
        tempVarCount += quadruples.size() - 1;
        for (Quadruple quadruple : quadruples) {
            if (!quadruple.operator().equals("=")) {
                code.append(dataType).append(" ").append(quadruple.resultIdentifier()).append(";").append("\n");
            }
        }
        for (Quadruple quadruple : quadruples) {
            code.append(quadruple.toString()).append(";\n");
        }
    }

    @Override
    public String toString() {
        return code.toString();
    }
}
