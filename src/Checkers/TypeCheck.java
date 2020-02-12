package Checkers;

import java.util.ArrayList;
import java.util.List;

import Procesador.Entorno;
import Procesador.EntornoFuncion;
import Procesador.GlobalVariables;
import SimbolosNoTerminales.SimboloOperacion;
import SimbolosNoTerminales.SimboloParams;

public class TypeCheck {
	
	public static void checkBoolean(SimboloOperacion o) {
		if(!Tipo.Boolean.equals(o.getTipoSubyacente()))
			throw new Error("El tipo de la operación no es Boolean");
	}
	
	public static void lanzaErrorTypeMismatch(Tipo tipo1, Tipo tipo2) {
		throw new Error("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}
	
	public static void lanzaErrorReturnTypeVoid() {
		throw new Error("Se ha producido un error. La función no esperaba valor de retorno.");
	}
	
	public static void lanzaErrorReturnTypeMismatch(Tipo tipoPrevisto, Tipo tipo2) {
		throw new Error("Se ha producido un error al operar un tipo "+tipoPrevisto+" con un tipo "+tipo2);
	}
	
	public static void lanzaErrorParamNumberMismatch(String idFuncion) {
		throw new Error("El número de parámetros en la llamada a la función "+idFuncion+" no corresponde con los indicados en su definición");
	}
	
	public static void parameterMatch(String idFuncion, SimboloParams p) {
		Entorno entorno = GlobalVariables.entornoActual();
		EntornoFuncion entornoFuncion = entorno.fullGetFuncionEntorno(idFuncion);
		List<String> argumentos = entornoFuncion.getArgs();
		List<Tipo> parametros = new ArrayList<>();
		for(SimboloParams param = p; param != null; param = param.getNextParam()) {
			parametros.add(param.getTipoSubyacente());
		}
		if(argumentos.size() != parametros.size())
			lanzaErrorParamNumberMismatch(idFuncion);
		if(!argumentos.isEmpty()) {
			for(int i = 0; i < argumentos.size(); i++) {
				typesMatch(entornoFuncion.fullGet(argumentos.get(i)).getTipo(), parametros.get(i));
			}
		}
	}
	
	public static void returnTypeMatch(SimboloOperacion o) {
		EntornoFuncion entornoFuncion = (EntornoFuncion) GlobalVariables.entornoActual();
		Tipo tipoEsperado = entornoFuncion.getIdentificador().getTipo();
		Tipo tipoObtenido = o.getTipoSubyacente();
		if(Tipo.Void.equals(tipoEsperado))
			lanzaErrorReturnTypeVoid();
		if(!tipoEsperado.equals(tipoObtenido))
			lanzaErrorReturnTypeMismatch(tipoEsperado, tipoObtenido);
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
