package Checkers;

public enum TipoOperador {
	AritmeticoSuma,
	AritmeticoProducto,
	Comparador,
	Logico,
	ComparadorLogico;
	
	public static TipoOperador getTipoOperador(String operador) {
		switch(operador) {
			case "+":
			case "-":
				return TipoOperador.AritmeticoSuma;
			case "*":
			case "/":
				return TipoOperador.AritmeticoProducto;
			case "&&":
			case "||":
				return TipoOperador.Logico;
			case "==":
			case "!=":
				return TipoOperador.ComparadorLogico;
			case ">=":
			case "<=":
			case ">":
			case "<":
				return TipoOperador.Comparador;
			default:
				return null;
		}
	}
	
}
