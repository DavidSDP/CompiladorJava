package Checkers;

public enum TipoOperador {
	AritmeticoSuma,
	AritmeticoProducto,
	Comparador,
	Logico,
	ComparadorLogico;
	
	public static TipoOperador getTipoOperador(String operador) {
		switch(operador) {
			case "+": return TipoOperador.AritmeticoSuma;
			case "-": return TipoOperador.AritmeticoSuma;
			case "*": return TipoOperador.AritmeticoProducto;
			case "/": return TipoOperador.AritmeticoProducto;
			case "&&": return TipoOperador.Logico;
			case "||": return TipoOperador.Logico;
			case "==": return TipoOperador.ComparadorLogico;
			case ">=": return TipoOperador.Comparador;
			case "<=": return TipoOperador.Comparador;
			case "!=": return TipoOperador.ComparadorLogico;
			case ">": return TipoOperador.Comparador;
			case "<": return TipoOperador.Comparador;
			default:
				return null;
		}
	}
	
}
