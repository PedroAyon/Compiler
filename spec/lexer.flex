package generated;

import java_cup.runtime.Symbol;

%%

%public
%class Lexer
%type java_cup.runtime.Symbol
%line
%column
%unicode
%ignorecase
%cup
%char

%init{
    yyline = 1;
%init}

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

%eofval{
{return new Symbol(sym.EOF, null);}
%eofval}

IntegerLiteral = 0 | [1-9][0-9]*
DecimalLiteral = {IntegerLiteral}("."[0-9]+)?
StringLiteral = \"([^\"\\]|\\.)*\"
CharLiteral = '[\x20-\x7E]'
Identifier = [a-zA-Z_][a-zA-Z0-9_]*

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
CommentContent       = ( [^*] | \*+ [^/*] )*
DocumentationComment = "/**" {CommentContent} "*"+ "/"
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}


%%

// Delimitadores
"," { return symbol(sym.COLON); }
";" { return symbol(sym.SEMICOLON); }
"{" { return symbol(sym.BRACKET_LEFT); }
"}" { return symbol(sym.BRACKET_RIGHT); }
"(" { return symbol(sym.PARENT_LEFT); }
")" { return symbol(sym.PARENT_RIGHT); }

// Operadores Aritméticos
"*" { return symbol(sym.TIMES, yytext()); }
"**" { return symbol(sym.POW, yytext()); }
"+" { return symbol(sym.PLUS, yytext()); }
"-" { return symbol(sym.MINUS, yytext()); }
"/" { return symbol(sym.DIV, yytext()); }
"//" { return symbol(sym.IDIV, yytext()); }
"%" { return symbol(sym.MOD, yytext()); }

// Operadores Incremento
"++" { return symbol(sym.INC, yytext()); }
"--" { return symbol(sym.DEC, yytext()); }

// Operadores Lógicos
"&&" { return symbol(sym.AND, yytext()); }
"!" { return symbol(sym.NOT, yytext()); }
"||" { return symbol(sym.OR, yytext()); }

// Operadores Relacionales
"<" { return symbol(sym.LESS_THAN, yytext()); }
">" { return symbol(sym.MORE_THAN, yytext()); }
"<=" { return symbol(sym.LESS_OR_EQ, yytext()); }
">=" { return symbol(sym.MORE_OR_EQ, yytext()); }
"==" { return symbol(sym.EQUALS, yytext()); }
"!=" { return symbol(sym.DIFFERENT_THAN, yytext()); }

// Operadores Generales
"=" { return symbol(sym.ASSIGN, yytext()); }

"int" { return symbol(sym.INT, yytext()); }
"float" { return symbol(sym.FLOAT, yytext()); }
"double" { return symbol(sym.DOUBLE, yytext()); }
"char" { return symbol(sym.CHAR, yytext()); }
"string" { return symbol(sym.STRING, yytext()); }
"bool" { return symbol(sym.BOOL, yytext()); }

"const" { return symbol(sym.CONST); }

"for" { return symbol(sym.FOR); }
"while" { return symbol(sym.WHILE); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }

"true" | "false" { return symbol(sym.BOOLEAN_LITERAL, yytext()); }
{IntegerLiteral} { return symbol(sym.INTEGER_LITERAL, yytext()); }
{DecimalLiteral} { return symbol(sym.DECIMAL_LITERAL, yytext()); }
{CharLiteral} {return symbol(sym.CHAR_LITERAL, yytext());}
{StringLiteral} {return symbol(sym.STRING_LITERAL, yytext());}
{Identifier} { return symbol(sym.IDENTIFIER, yytext()); }

{Comment} { /* ignore */ }

{WhiteSpace} { /* ignore */ }

/* error fallback */
[^] { throw new Error("Illegal character <"+ yytext() + ">"); }