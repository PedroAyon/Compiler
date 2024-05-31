package generation_schemes;

public class ValueUtils {

    public static String defaultForDataType(String t) {
        return switch (t) {
            case "int", "bool" -> "0";
            case "double" -> "0.0";
            case "string" -> "\"\"";
            case "char" -> "'0'";
            default -> null;
        };
    }
}
