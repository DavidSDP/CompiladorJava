package analisisLexico;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import analisisSintactico.sym;


%%

%public
%class Scanner

%unicode
%line
%column
%cup
%type ComplexSymbol
%eofval{
	return symbol(sym.EOF);
%eofval}

%{
    private ComplexSymbolFactory symbolFactory;
    
    public Scanner(ComplexSymbolFactory sf, java.io.Reader reader){
		this(reader);
        symbolFactory = sf;
    }
    
    public ComplexSymbol symbol(int type){
		return (ComplexSymbol) symbolFactory.newSymbol(sym.terminalNames[type], type,
						new Location(yyline+1, yycolumn+1, yychar),
						new Location(yyline+1, yycolumn + yylength(), yychar + yylength())
		);
    }
    
    public ComplexSymbol symbol(int type, String lexem){
		return (ComplexSymbol) symbolFactory.newSymbol(sym.terminalNames[type], type,
						new Location(yyline+1, yycolumn+1, yychar),
						new Location(yyline+1, yycolumn + yylength(), yychar + yylength()),
						lexem
		);
    }
%}

/* definiciones regulares */

FINLINEA	= \r|\n|\r\n
ESPACIO		= {FINLINEA} | [ \t\f]

TCLASS		=	"class"
TVAR		=	"int"|"boolean"|"String"
TVOID		=	"void"
TRETURN		=	"return"
TIF			= 	"if"
TELSE		= 	"else"

LLAVEIZQ	=	"{"
LLAVEDER	=	"}"

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

VSTRING		=	[\"][A-Za-z0-9_]*[\"]
VNUMERO		=	(0|[1-9][0-9]*)

VID			=	[A-Za-z][A-Za-z0-9_]*

%%

{ESPACIO}		{/* nada que hacer */}

{TCLASS}		{return symbol(sym.TCLASS);}
{TVAR}			{return symbol(sym.TVAR, this.yytext());}
{TVOID}			{return symbol(sym.TVOID);}
{TRETURN}		{return symbol(sym.TRETURN);}
{TIF}			{return symbol(sym.TIF);}
{TELSE}			{return symbol(sym.TELSE);}

{LLAVEIZQ}		{return symbol(sym.LLAVEIZQ);}
{LLAVEDER}		{return symbol(sym.LLAVEDER);}

{IGUAL}			{return symbol(sym.IGUAL);}
{PUNTOCOMA}		{return symbol(sym.PUNTOCOMA);}
{COMA}			{return symbol(sym.COMA);}

{PARENIZQ}		{return symbol(sym.PARENIZQ);}
{PARENDER}		{return symbol(sym.PARENDER);}

{OPSUMA}		{return symbol(sym.OPSUMA, this.yytext());}
{OPPROD}		{return symbol(sym.OPPROD, this.yytext());}

{COMPARADOR}	{return symbol(sym.COMPARADOR, this.yytext());}

{OPLOGICO}		{return symbol(sym.OPLOGICO, this.yytext());}

{VBOOLEANO}		{return symbol(sym.VBOOLEANO, this.yytext());}

{VSTRING}		{return symbol(sym.VSTRING, this.yytext());}
{VNUMERO}		{return symbol(sym.VNUMERO, this.yytext());}
{VID}			{return symbol(sym.VID, this.yytext());}

[^]				{throw new Error("Carácter no reconocido: <"+yytext()+">"); }