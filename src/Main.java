import generated.IntermediateCodeParser;
import generated.Lexer;
import generated.Parser;
import generated.sym;
import java_cup.runtime.Symbol;

import java.io.*;


public class Main {
    public static void main(String[] args) {
        try {
            String testFilePath = "C:\\Users\\pedro\\Dev\\Compiler\\test\\input\\test1.in";
            FileReader fileReader = new FileReader(testFilePath);
            Lexer lexer = new Lexer(fileReader);
            parseSourceFile(lexer);
            String intermediateCodeFilePath = "C:\\Users\\pedro\\Dev\\Compiler\\test\\output\\intermediate_code.out";
            fileReader = new FileReader(intermediateCodeFilePath);
            lexer = new Lexer(fileReader);
            parseIntermediateCode(lexer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void read_lexeme(Lexer lexer) {
        Symbol symbol = null;
        do {
            try {
                symbol = lexer.next_token();
                printSymbol(symbol);
            } catch (Exception e) {
                String message = e.getMessage();
                if (symbol != null) {
                    message += " at line " + (symbol.left + 1) + ", column " + symbol.right;
                }
                System.out.println(message);
            }
        } while (symbol.sym - 1 != Lexer.YYEOF);
    }

    private static void parseSourceFile(Lexer lexer) throws Exception {
        Parser parser = new Parser(lexer);
        parser.parse();
        // semantics.printIdentifiers();
        String code = parser.getIntermediateCode();
        //System.out.println(code);
        writeFile("C:\\Users\\pedro\\Dev\\Compiler\\test\\output\\intermediate_code.out", code);
    }

    private static void parseIntermediateCode(Lexer lexer) throws Exception {
        IntermediateCodeParser parser = new IntermediateCodeParser(lexer);
        parser.parse();
        String assemblyCode = parser.getAssemblyCode();
        // System.out.println(assemblyCode);
        writeFile("C:\\Users\\pedro\\Dev\\Compiler\\test\\output\\program.asm", assemblyCode);
    }

    private static void writeFile(String filePath, String code) {
        File file = new File(filePath);

        // Check if file exists and delete if it does
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Failed to delete existing file.");
                return;
            }
        }

        // Write content to the new file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }


    private static void printSymbol(Symbol symbol) {
        int tokenId = symbol.sym;
        String tokenText = symbol.value != null ? symbol.value.toString() : "";
        int line = symbol.left;
        int column = symbol.right;

        System.out.println("Token: " + sym.terminalNames[tokenId]);
        System.out.println("TokenID: " + tokenId);
        if (!tokenText.isEmpty()) System.out.println("Token Text: " + tokenText);
        System.out.print("Line: " + line);
        System.out.println(", Column: " + column);
        System.out.println();
    }

}