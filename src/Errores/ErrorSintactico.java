package Errores;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

public class ErrorSintactico extends ErrorProcesador{
	
	private static final long serialVersionUID = 1L;
	
	private ComplexSymbol simboloInesperado;
	
	public ErrorSintactico(ComplexSymbol simboloInesperado) {
		super("");
		this.simboloInesperado = simboloInesperado;
	}
	
	@Override
	protected String getErrorLine() {
		StringBuffer sb = new StringBuffer();
		sb.append("Linea [" + simboloInesperado.getLeft().getLine()+"], ");
		sb.append("Columna [" + simboloInesperado.getLeft().getColumn()+"], ");
		sb.append("se ha encontrado un Token {");
		sb.append(simboloInesperado.getName() + ", " + simboloInesperado.value + "} inesperado.");
		return sb.toString();
	}
	
}
