package Checkers;

import SimbolosNoTerminales.SimboloOperacion;

public class TypeCheck {
	
	public static void checkBoolean(SimboloOperacion o) {
		if(!Tipo.Boolean.equals(o.getTipoSubyacente()))
			throw new Error("El tipo de la operación no es Boolean");
	}
	
	public static void lanzaErrorTypeMismatch(Tipo tipo1, Tipo tipo2) {
		throw new Error("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}
	
	public static void typesMatch(Tipo tipo, TipoOperador tipoOperador) {
		if(TipoOperador.ComparadorLogico.equals(tipoOperador)) {
			if(!Tipo.Boolean.equals(tipo)
					&& !Tipo.Integer.equals(tipo)
					&& !Tipo.String.equals(tipo))
				lanzaErrorTypeMismatch(tipo, Tipo.Comparable);
		}else if(TipoOperador.Comparador.equals(tipoOperador)) {
			if(!Tipo.Integer.equals(tipo))
				lanzaErrorTypeMismatch(tipo, Tipo.Integer);
		}else if(TipoOperador.Logico.equals(tipoOperador)) {
			if(!Tipo.Boolean.equals(tipo))
				lanzaErrorTypeMismatch(tipo, Tipo.Boolean);
		}else if(TipoOperador.AritmeticoSuma.equals(tipoOperador) || TipoOperador.AritmeticoProducto.equals(tipoOperador)) {
			if(!Tipo.Integer.equals(tipo))
				lanzaErrorTypeMismatch(tipo, Tipo.Integer);
		}
	}
	
	public static void typesMatch(Tipo tipo1, Tipo tipo2) {
		if(!tipo1.equals(tipo2))
			lanzaErrorTypeMismatch(tipo1, tipo2);
	}
	
}
