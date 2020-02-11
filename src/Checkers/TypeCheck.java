package Checkers;

import Procesador.Tipo;
import SimbolosNoTerminales.SimboloOperacion;

public class TypeCheck {
	
	public static void checkBoolean(SimboloOperacion o) {
		if(!Tipo.Boolean.equals(o.getTipoSubyacente()))
			throw new Error("El tipo de la operación no es Boolean");
	}
	
	public static Tipo getTipoOperador(String operador) {
		switch(operador) {
			case "+": return Tipo.Integer;
			case "-": return Tipo.Integer;
			case "*": return Tipo.Integer;
			case "/": return Tipo.Integer;
			case "&&": return Tipo.Boolean;
			case "||": return Tipo.Boolean;
			case "==": return Tipo.Comparable;
			case ">=": return Tipo.Integer;
			case "<=": return Tipo.Integer;
			case "!=": return Tipo.Comparable;
			case ">": return Tipo.Integer;
			case "<": return Tipo.Integer;
			default:
				return null;
		}
	}
	
	public static void lanzaErrorTypeMismatch(Tipo tipo1, Tipo tipo2) {
		throw new Error("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}

	public static void typesMatch(Tipo tipoSubyacente, Tipo tipoOperador) {
		if(Tipo.Comparable.equals(tipoSubyacente)) {
			if(!Tipo.Boolean.equals(tipoOperador) && !Tipo.Integer.equals(tipoOperador))
				lanzaErrorTypeMismatch(tipoSubyacente, tipoOperador);
		}
		if(Tipo.Comparable.equals(tipoOperador)) {
			if(!Tipo.Boolean.equals(tipoSubyacente) && !Tipo.Integer.equals(tipoSubyacente))
				lanzaErrorTypeMismatch(tipoSubyacente, tipoOperador);
		}
		if(!tipoSubyacente.equals(tipoOperador))
			lanzaErrorTypeMismatch(tipoSubyacente, tipoOperador);
	}
	
}
