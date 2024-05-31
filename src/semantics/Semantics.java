package semantics;

import generated.Lexer;
import generated.sym;
import java_cup.runtime.Symbol;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Objects;

public class Semantics {
    private ArrayList<Identifier> identifiers;

    public Semantics() {
        identifiers = new ArrayList<>();
    }

    public void addIdentifier(String name, String dataType, String role) throws SemanticException {
        Identifier id = new Identifier(name, dataType, role, "global");
        validateIdentifierDoesNotExist(id.getName());
        identifiers.add(id);
    }

    public void validateIdentifierExist(String id) throws SemanticException {
        boolean found = false;
        for (Identifier i : identifiers) {
            if (Objects.equals(i.getName(), id)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new SemanticException("The identifier '" + id + "' does not exists in the current scope.");
        }
    }

    public void validateIdentifierDoesNotExist(String id) throws SemanticException {
        for (Identifier i : identifiers) {
            if (Objects.equals(i.getName(), id)) {
                throw new SemanticException("The identifier '" + id + "' already exists in the current scope.");
            }
        }
    }

    public void validateIdentifierIsNotConstant(String id) throws SemanticException {
        for (Identifier i : identifiers) {
            if (Objects.equals(i.getName(), id) && Objects.equals(i.getRole(), "constant")) {
                throw new SemanticException("'" + id + "' is a constant.");
            }
        }
    }

    public void validateExpressionMatchesIdentifierDataType(String identifier, String expression) throws SemanticException {
        Identifier i = getIdentifier(identifier);
        assert i != null;
        String dataType = i.getDataType();
        int dataTypeLiteralToken = switch (dataType) {
            case "bool" -> sym.BOOLEAN_LITERAL;
            case "string" -> sym.STRING_LITERAL;
            case "int" -> sym.INTEGER_LITERAL;
            case "double" -> sym.DECIMAL_LITERAL;
            case "char" -> sym.CHAR_LITERAL;
            default -> 0;
        };
        StringReader reader = new StringReader(expression);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = null;
        do {
            try {
                symbol = lexer.next_token();
                int token = symbol.sym;
                if (isLiteralValue(token) && symbol.sym != dataTypeLiteralToken) {
                    throw new SemanticException(symbol.value.toString() + " cannot be casted as '" + dataType + "'.");
                } else if (token == sym.IDENTIFIER) { // if it's a variable/constant
                    Identifier i1 = getIdentifier(symbol.value.toString());
                    assert i1 != null;
                    if (!Objects.equals(i1.getDataType(), dataType)) {
                        throw new SemanticException(i1.getDataType() + " cannot be casted as '" + dataType + "'.");
                    }
                } else if (isBooleanOrLogicOperator(token) && !dataType.equals("bool")) {
                    throw new SemanticException("boolean operators are not allowed for '" + dataType + "' datatype.");
                } else if (dataType.equals("string") && isArithmeticOperator(token) && token != sym.PLUS) {
                    throw new SemanticException("this operator is not allowed for '" + dataType + "' datatype.");
                } else if (dataType.equals("char") && isArithmeticOperator(token) && !(token == sym.PLUS || token == sym.MINUS)) {
                    throw new SemanticException("this operator is not allowed for '" + dataType + "' datatype.");
                }


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } while (symbol.sym - 1 != Lexer.YYEOF);

    }

    // meant to use after validating that all identifier in the expression already exist.
    public Identifier getIdentifier(String identifier) {
        for (Identifier i : identifiers) {
            if (Objects.equals(i.getName(), identifier)) {
                return i;
            }
        }
        return null;
    }

    private boolean isLiteralValue(int token) {
        return token == sym.INTEGER_LITERAL || token == sym.DECIMAL_LITERAL || token == sym.CHAR_LITERAL || token == sym.STRING_LITERAL || token == sym.BOOLEAN_LITERAL;
    }

    private boolean isBooleanOrLogicOperator(int token) {
        return token == sym.AND || token == sym.OR || token == sym.NOT
                || token == sym.MORE_THAN || token == sym.LESS_THAN || token == sym.MORE_OR_EQ || token == sym.LESS_OR_EQ
                || token == sym.EQUALS || token == sym.DIFFERENT_THAN;
    }

    private boolean isArithmeticOperator(int token) {
        return token == sym.PLUS || token == sym.MINUS || token == sym.TIMES || token == sym.DIV || token == sym.MOD || token == sym.IDIV || token == sym.POW;
    }
}
