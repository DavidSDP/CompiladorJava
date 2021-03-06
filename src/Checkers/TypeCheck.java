package Checkers;

import java.util.ArrayList;
import java.util.List;

import Errores.ErrorSemantico;
import Procesador.*;
import SimbolosNoTerminales.SimboloOperacion;
import SimbolosNoTerminales.SimboloParams;

public class TypeCheck {
	
	public static void checkBoolean(SimboloOperacion o, Boolean esBucle) throws ErrorSemantico {
		String sentencia = esBucle?"WHILE":"IF";
		if(!Tipo.Boolean.equals(o.getTipoSubyacente().getTipo()))
			throw new ErrorSemantico("El valor a evaluar por la sentencia "+sentencia+" no es de tipo Boolean");
	}
	
	public static void lanzaErrorArrayNoInicializable() throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error, el tipo Array no puede ser inicializado");
	}
	
	public static void lanzaErrorConstanteNoInicializada(String id, String tipo) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error, la constante {" + id + "} de tipo " + Tipo.getTipo(tipo) + " requiere ser inicializada");
	}
	
	public static void lanzaErrorTypeMismatch(TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}

	public static void lanzaErrorTypeMismatch(TipoObject tipo1, Tipo tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error al operar un tipo "+tipo1+" con un tipo "+tipo2);
	}
	
	public static void lanzaErrorParamTypeMismatch(String id, TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error en los parámetros de la función {"+id+"}, al tratar de asignar un tipo "+tipo2+" a un tipo "+tipo1);
	}
	
	public static void lanzaErrorTypeMismatchAsignacion(String id, TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error en el id {"+id+"} al tratar de asignar un tipo "+tipo2+" a un tipo "+tipo1);
	}
	
	public static void lanzaErrorReturnTypeVoid(String idFuncion) throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error. La función {"+idFuncion+"} no esperaba valor de retorno.");
	}
	
	private static void lanzaErrorReturnTypeBadUse() throws ErrorSemantico {
		throw new ErrorSemantico("Se ha producido un error. No se esperaba valor de retorno.");
	}
	
	public static void lanzaErrorReturnTypeMismatch(String idFuncion, TipoObject tipoPrevisto, TipoObject tipo2) throws ErrorSemantico {
		throw new ErrorSemantico("El valor de retorno de la función {"+idFuncion+"} debería ser de tipo "+tipoPrevisto+", en cambio se encontró un valor de tipo "+tipo2);
	}
	
	public static void lanzaErrorParamNumberMismatch(String idFuncion) throws ErrorSemantico {
		throw new ErrorSemantico("El número de parámetros en la llamada a la función "+idFuncion+" no corresponde con los indicados en su definición");
	}
	
	public static void parameterMatch(String idFuncion, SimboloParams p) throws ErrorSemantico {
		Entorno entorno = GlobalVariables.entornoActual();
		List<TipoObject> parametros = new ArrayList<>();
		for(SimboloParams param = p; param != null; param = param.getNextParam()) {
			parametros.add(param.getTipoSubyacente());
		}
		DeclaracionFuncion funcion = entorno.fullGetFuncion(idFuncion, parametros);

		if (funcion == null) {
			throw new ErrorSemantico("La función "+idFuncion+" no existe ( Revisar los parámetros ).");
		}
	}
	
	public static void returnTypeMatch(SimboloOperacion o) throws ErrorSemantico {
		Entorno entornoActual = GlobalVariables.entornoActual();
		Declaracion identificadorFuncion = entornoActual.getIdentificadorFuncionRetorno();
		if(identificadorFuncion == null)
			lanzaErrorReturnTypeBadUse();
		TipoObject tipoObtenido = o.getTipoSubyacente();
		if(Tipo.Void.equals(identificadorFuncion.getTipo()))
			lanzaErrorReturnTypeVoid(identificadorFuncion.getId().getId());
		if(!identificadorFuncion.getTipo().equals(tipoObtenido))
			lanzaErrorReturnTypeMismatch(identificadorFuncion.getId().getId(), identificadorFuncion.getTipo(), tipoObtenido);
	}

	public static void typesMatch(TipoObject tipo, TipoOperador tipoOperador) throws ErrorSemantico {
		if(TipoOperador.ComparadorLogico.equals(tipoOperador)) {
			if(!Tipo.Boolean.equals(tipo.getTipo())
					&& !Tipo.Integer.equals(tipo.getTipo())
					&& !Tipo.String.equals(tipo.getTipo()))
				lanzaErrorTypeMismatch(tipo, Tipo.Comparable);
		}else if(TipoOperador.Comparador.equals(tipoOperador)) {
			if(!Tipo.Integer.equals(tipo.getTipo()))
				lanzaErrorTypeMismatch(tipo, Tipo.Integer);
		}else if(TipoOperador.Logico.equals(tipoOperador)) {
			if(!Tipo.Boolean.equals(tipo.getTipo()))
				lanzaErrorTypeMismatch(tipo, Tipo.Boolean);
		}else if(TipoOperador.AritmeticoSuma.equals(tipoOperador) || TipoOperador.AritmeticoProducto.equals(tipoOperador)) {
			if(!Tipo.Integer.equals(tipo.getTipo()))
				lanzaErrorTypeMismatch(tipo, Tipo.Integer);
		}
	}
	
	public static void paramTypesMatch(String id, TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorParamTypeMismatch(id, tipo1, tipo2);
	}
	
	public static void typesMatchAsignacion(String id, TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorTypeMismatchAsignacion(id, tipo1, tipo2);
	}
	
	public static void typesMatchAsignacionArray(String id, TipoObject tipoOperacion) throws ErrorSemantico {
		DeclaracionArray declaracion = (DeclaracionArray) GlobalVariables.entornoActual().fullGet(id);
		if(!(declaracion.getTipoDato().equals(tipoOperacion)))
			lanzaErrorTypeMismatchAsignacion(id, declaracion.getTipoDato(), tipoOperacion);
	}
	
	public static void typesMatch(TipoObject tipo1, TipoObject tipo2) throws ErrorSemantico {
		if(!tipo1.equals(tipo2))
			lanzaErrorTypeMismatch(tipo1, tipo2);
	}
	
}
