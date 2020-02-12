package Procesador;

import java.util.EmptyStackException;
import java.util.Stack;

import Checkers.Tipo;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArgs;

public class GlobalVariables{
		
		public static final String FICHERO_TOKENS = "..\\output\\FicheroTokens.txt";
		public static final String FICHERO_ARBOL = "..\\output\\ArbolSintactico.dot";
		public static final String FICHERO_ERRORES = "..\\output\\Errores.txt";
		
		public static Boolean DEBUG_MODE = true;
		public static Boolean hayErrores = false;
		
		private static Integer _idnodoIncremental = 0;
		private static Integer CONTADOR = 1;
		private static Stack<Entorno> pilaEntornos = new Stack<>();
		
		public static Entorno entornoActual() {
			try {
				return pilaEntornos.peek();
			}catch(EmptyStackException e) {
				return null;
			}
		}
		
		public static void asignaID(String id, String tipo) throws ErrorSemantico {
			Entorno top = entornoActual();
			top.put(Tipo.getTipo(tipo), id);
		}
		
		public static void asignaID(String id, Tipo tipo) throws ErrorSemantico {
			Entorno top = entornoActual();
			top.put(tipo, id);
		}
		
		public static void asignaFuncionID(String idFuncion, String tipo) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			top.putFuncion(Tipo.getTipo(tipo), idFuncion);
		}
		
		public static void asignaFuncionID(String idFuncion, Tipo tipo) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			top.putFuncion(tipo, idFuncion);
		}
		
		// Llamar una vez dentro del entorno de la función
		public static void asignaEntornoFuncionID(String idFuncion) throws ErrorSemantico {
			EntornoFuncion top = (EntornoFuncion) entornoActual();
			((EntornoClase)top.getEntornoAnterior()).putFuncionEntorno(idFuncion, top);
		}
		
		public static void asignaFuncionArgs(String idFuncion, SimboloArgs args) throws ErrorSemantico {
			EntornoFuncion top = (EntornoFuncion) entornoActual();
			for(SimboloArgs a = args; a != null; a = a.getNextArg()) {
				top.putFuncionArgs(idFuncion, a.getId());
			}
		}
		
		public static void asignaClaseID(String id) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			top.putClase(id);
		}
		
		public static void compruebaID(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Identificador i = top.fullGet(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un símbolo declarado en el entorno");
		}
		
		public static void compruebaFuncionID(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Identificador i = top.fullGetFuncion(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un símbolo de función declarado en el entorno");
		}
		
		public static void compruebaClaseID(String id) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			Identificador i = top.fullGetClase(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un símbolo de clase declarado en el entorno");
		}
		
		public static void entraBloqueClase(Identificador identificadorClase) {
			EntornoClase e = new EntornoClase(entornoActual(), identificadorClase);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueClase() {
			EntornoClase popped = (EntornoClase) pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void entraBloqueFuncion(Identificador identificadorFuncion) {
			EntornoFuncion e = new EntornoFuncion((EntornoClase)entornoActual(), identificadorFuncion);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueFuncion() {
			EntornoFuncion popped = (EntornoFuncion) pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static Integer getIdentificador() {
			return CONTADOR++;
		}
		
		public static Integer getNodoIdentificadorUnico() {
			return _idnodoIncremental++;
		}
		
	}