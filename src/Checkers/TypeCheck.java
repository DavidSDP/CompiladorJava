package Checkers;

import java.util.ArrayList;
import java.util.List;

import Errores.ErrorSemantico;
import Procesador.Entorno;
import Procesador.EntornoFuncion;
import Procesador.GlobalVariables;
import SimbolosNoTerminales.SimboloOperacion;
import SimbolosNoTerminales.SimboloParams;

public class TypeCheck {
	
	public static void checkBoolean(SimboloOperacion o) throws ErrorSemantico {
		if(!Tipo.Boolean.equals(o.getTipoSubyacente()))
			throw new ErrorSemantico("El valor a evaluar por la sentencia IF no es de tipo Boolean");
	}
	
	public static void lanzaErrorTypeMismatch(Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}
	
	public static void lanzaErrorParamTypeMismatch(String id, Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error en los parámetros de la función {"+id+"}, al tratar de asignar un tipo "+tipo2+" a un tipo "+tipo1);
	}
	
	public static void lanzaErrorTypeMismatchAsignacion(String id, Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error en el id {"+id+"} al tratar de asignar un tipo "+tipo2+" a un tipo "+tipo1);
	}
	
	public static void lanzaErrorReturnTypeVoid(String idFuncion) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error. La función {"+idFuncion+"} no esperaba valor de retorno.");
	}
	
	public static void lanzaErrorReturnTypeMismatch(String idFuncion, Tipo tipoPrevisto, Tipo tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("El valor de retorno de la función {"+idFuncion+"} debería ser de tipo "+tipoPrevisto+", en cambio se encontró un valor de tipo "+tipo2);
	}
	
	public static void lanzaErrorParamNumberMismatch(String idFuncion) throws ErrorSemantico {
		throw new ErrorSemantico("El número de parámetros en la llamada a la función "+idFuncion+" no corresponde con los indicados en su definición");
	}
	
	public static void parameterMatch(String idFuncion, SimboloParams p) throws ErrorSemantico {
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
				paramTypesMatch(idFuncion, entornoFuncion.fullGet(argumentos.get(i)).getTipo(), parametros.get(i));
			}
		}
	}
	
	public static void returnTypeMatch(SimboloOperacion o) throws ErrorSemantico {
		EntornoFuncion entornoFuncion = (EntornoFuncion) GlobalVariables.entornoActual();
		Tipo tipoEsperado = entornoFuncion.getIdentificador().getTipo();
		Tipo tipoObtenido = o.getTipoSubyacente();
		if(Tipo.Void.equals(tipoEsperado))
			lanzaErrorReturnTypeVoid(entornoFuncion.getIdentificador().getId());
		if(!tipoEsperado.equals(tipoObtenido))
			lanzaErrorReturnTypeMismatch(entornoFuncion.getIdentificador().getId(), tipoEsperado, tipoObtenido);
	}
	
	public static void typesMatch(Tipo tipo, TipoOperador tipoOperador) throws ErrorSemantico {
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
	
	public static void paramTypesMatch(String id, Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorParamTypeMismatch(id, tipo1, tipo2);
	}
	
	public static void typesMatchAsignacion(String id, Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorTypeMismatchAsignacion(id, tipo1, tipo2);
	}
	
	public static void typesMatch(Tipo tipo1, Tipo tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorTypeMismatch(tipo1, tipo2);
	}
	
}
