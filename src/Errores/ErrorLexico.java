package Errores;

public class ErrorLexico extends ErrorProcesador{
	
	private static final long serialVersionUID = 1L;
	
	private String yytext;
	private Integer yyline;
	private Integer yycolumn;
	
	public ErrorLexico(String yytext, Integer yyline, Integer yycolumn) {
		super("");
		this.yytext = yytext;
		this.yyline = yyline;
		this.yycolumn = yycolumn;
	}
	
	@Override
	public String getErrorLine() {
		StringBuffer sb = new StringBuffer();
		sb.append("Línea ["+this.yyline+"], Columna ["+this.yycolumn+"], el Token {"+this.yytext+"} no ha sido reconocido.");
		return sb.toString();
	}
	
}
