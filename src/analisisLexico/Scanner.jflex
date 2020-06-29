package analisisLexico;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import analisisSintactico.sym;
import Ejecucion.FicheroTokens;
import Errores.ErrorLexico;
import Errores.ErrorHandler;
import java.io.IOException;

%%

%public
%class Scanner

%throws ErrorLexico
%unicode
%line
%column
%cup
%type ComplexSymbol
%eofval{
	return symbol(sym.EOF);
%eofval}

%{
    private StringBuffer string = new StringBuffer();
    private ComplexSymbolFactory symbolFactory;
    
    public Scanner(ComplexSymbolFactory sf, java.io.Reader reader){
		this(reader);
        symbolFactory = sf;
    }
    
    public ComplexSymbol symbol(int type) throws IOException{
    	ComplexSymbol symbol = (ComplexSymbol) symbolFactory.newSymbol(sym.terminalNames[type], type,
						new Location(yyline+1, yycolumn+1, yychar),
						new Location(yyline+1, yycolumn + yylength(), yychar + yylength())
		);
    	FicheroTokens.almacenaToken(symbol);
		return symbol;
    }
    
    public ComplexSymbol symbol(int type, String lexem) throws IOException{
    	ComplexSymbol symbol = (ComplexSymbol) symbolFactory.newSymbol(sym.terminalNames[type], type,
						new Location(yyline+1, yycolumn+1, yychar),
						new Location(yyline+1, yycolumn + yylength(), yychar + yylength()),
						lexem
		);
    	FicheroTokens.almacenaToken(symbol);
		return symbol;
    }
%}

/* definiciones regulares */

FINLINEA	= \r|\n|\r\n
ESPACIO		= {FINLINEA} | [ \t\f]

TCLASS		=	"class"
TVAR		=	"int"|"boolean"|"String"
TFINAL		=	"final"
TVOID		=	"void"
TRETURN		=	"return"
TIF			= 	"if"
TELSE		= 	"else"
TWHILE		=	"while"

FUNCTION		=	"function"

LLAVEIZQ	=	"{"
LLAVEDER	=	"}"

CORCHIZQ 	=	"["
CORCHDER 	=	"]"

IGUAL		=	"="
PUNTOCOMA	=	";"
COMA		=	","

PARENIZQ	=	"("
PARENDER	=	")"

OPSUMA		=	"+"|"-"
OPPROD		=	"*"|"/"

COMPARADOR	=	"=="|"!="|">"|"<"|">="|"<="
OPLOGICO	=	"&&"|"||"
VBOOLEANO	=	"true"|"false"

VSTRING		=	[\"][A-Za-z0-9_ ]*[\"]
COMILLAS    =   [\"]
VNUMERO		=	0|[-]?[1-9][0-9]*

VID			=	[A-Za-z][A-Za-z0-9_]*


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]


/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}


TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

%state STRING

%%

{ESPACIO}		{/* nada que hacer */}
<YYINITIAL> {
    {TCLASS}		{return symbol(sym.TCLASS);}
    {TVAR}			{return symbol(sym.TVAR, this.yytext());}
    {TFINAL}		{return symbol(sym.TFINAL);}
    {TVOID}			{return symbol(sym.TVOID);}
    {TRETURN}		{return symbol(sym.TRETURN);}
    {TIF}			{return symbol(sym.TIF);}
    {TELSE}			{return symbol(sym.TELSE);}

    {TWHILE}		{return symbol(sym.TWHILE);}

    {FUNCTION}		{return symbol(sym.FUNCTION);}

    {LLAVEIZQ}		{return symbol(sym.LLAVEIZQ);}
    {LLAVEDER}		{return symbol(sym.LLAVEDER);}

    {CORCHIZQ}		{return symbol(sym.CORCHIZQ);}
    {CORCHDER}		{return symbol(sym.CORCHDER);}

    {IGUAL}			{return symbol(sym.IGUAL);}
    {PUNTOCOMA}		{return symbol(sym.PUNTOCOMA);}
    {COMA}			{return symbol(sym.COMA);}

    {PARENIZQ}		{return symbol(sym.PARENIZQ);}
    {PARENDER}		{return symbol(sym.PARENDER);}

    {OPSUMA}		{return symbol(sym.OPSUMA, this.yytext());}
    {OPPROD}		{return symbol(sym.OPPROD, this.yytext());}

    {COMILLAS}      {yybegin(STRING); string.setLength(0);}

    {COMPARADOR}	{return symbol(sym.COMPARADOR, this.yytext());}

    {OPLOGICO}		{return symbol(sym.OPLOGICO, this.yytext());}

    {VBOOLEANO}		{return symbol(sym.VBOOLEANO, this.yytext());}
}
<STRING> {
    {COMILLAS}		{ yybegin(YYINITIAL); return symbol(sym.VSTRING, string.toString());}
    [^\n\r\"\\]+		{ string.append(yytext());}
}
{VNUMERO}		{return symbol(sym.VNUMERO, this.yytext());}
{VID}			{return symbol(sym.VID, this.yytext());}

{Comment} { /* ignore */ }

[^]				{ ErrorHandler.reportaError(new ErrorLexico(this.yytext(), this.yyline, this.yycolumn)); }
