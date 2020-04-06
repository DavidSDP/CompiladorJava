package Procesador;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

import Checkers.Tipo;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArgDecl;
import SimbolosNoTerminales.SimboloArgs;
import SimbolosNoTerminales.SimboloArray;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalVariables {
		
                // Cuidado! El path se resuelve aqui. Asi que si en algun momento
                // se cambia el directorio de la ejecucion probablemente no sea correcto
                // donde se estan guardando los ficheros
                public static final Path outputDir = Paths.get("output");
		public static final Path FICHERO_TOKENS = outputDir.resolve("FicheroTokens.txt");
		public static final Path FICHERO_ARBOL = outputDir.resolve("ArbolSintactico.dot");
		public static final Path FICHERO_ERRORES = outputDir.resolve("Errores.txt");
		public static final Path FICHERO_ENTORNOS = outputDir.resolve("Entornos.txt");
		public static final Path FICHERO_INTERMEDIO = outputDir.resolve("codigo_intermedio.txt");
		
		public static Boolean DEBUG_MODE = true;
		public static Boolean hayErrores = false;
                
                private static Integer SecuenciaIdEtiqueta = 0;
		
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
		
		public static void declaraBuiltInFunctions(EntornoClase raiz) throws ErrorSemantico, IOException {
			asignaBuiltInFuncionID("read", Tipo.String, null);
			asignaBuiltInFuncionID("write", Tipo.Void, new SimboloArgs(new SimboloArgDecl("input", Tipo.String, null), null, true));
			asignaBuiltInFuncionID("integerToString", Tipo.String, new SimboloArgs(new SimboloArgDecl("numero", Tipo.Integer, null), null, true));
			asignaBuiltInFuncionID("stringToInteger", Tipo.Integer, new SimboloArgs(new SimboloArgDecl("string", Tipo.String, null), null, true));
		}
		
		private static void asignaBuiltInFuncionID(String idFuncion, Tipo tipoRetorno, SimboloArgs args) throws ErrorSemantico, IOException {
			asignaFuncionID(idFuncion, tipoRetorno);
			entraBloqueFuncion(new Declaracion(new Identificador(idFuncion, idFuncion), tipoRetorno));
			asignaEntornoFuncionID(idFuncion);
			if(args != null) {
				for(SimboloArgs a = args; a != null; a = a.getNextArg()) {
					GlobalVariables.asignaID(a.getId(), a.getTipo());
				}
				asignaFuncionArgs(idFuncion, args);
			}
			saleBloqueFuncion(true);
		}

		public static Declaracion asignaID(String id, String tipo) throws ErrorSemantico {
                        return asignaID(id, Tipo.getTipo(tipo));
		}
		
		public static Declaracion asignaID(String id, Tipo tipo) throws ErrorSemantico {
			Entorno top = entornoActual();
			return top.put(tipo, id);
		}
		
		public static Declaracion asignaArray(String id, String tipo, SimboloArray simboloArrayDef) throws ErrorSemantico{                    
			Entorno top = entornoActual();
                        DeclaracionArray declArray = new DeclaracionArray(new Identificador(id, id), Tipo.getTipo(tipo), simboloArrayDef);
                        // TODO Esto es raro, de momento lo dejo así, pero vamos diría que esto no tiene que estar así
			top.getTablaIDs().put(id, declArray);
                        return declArray;
		}
		
		public static DeclaracionConstante asignaIDConstante(String id, String tipo, Object valor) throws ErrorSemantico {
			Entorno top = entornoActual();
			return top.putConstante(Tipo.getTipo(tipo), id, valor);
		}
		
		public static void asignaFuncionID(String idFuncion, String tipo) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			top.putFuncion(Tipo.getTipo(tipo), idFuncion);
		}
		
		public static void asignaFuncionID(String idFuncion, Tipo tipo) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			top.putFuncion(tipo, idFuncion);
		}
		
		// Llamar una vez dentro del entorno de la funciÃ³n
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
		
		public static Declaracion compruebaID(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Declaracion i = top.fullGet(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un sÃ­mbolo declarado en el entorno");
                        
                        return i;
		}
		
		public static void compruebaIDArray(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Declaracion d = top.fullGet(id);
			if(!(d instanceof DeclaracionArray))
				throw new ErrorSemantico("El id "+id+" no es un símbolo de array declarado en el entorno");
		}
		
		public static void compruebaAsignacionPermitida(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Declaracion i = top.fullGet(id);
			if(i instanceof DeclaracionConstante)
				throw new ErrorSemantico("El valor del identificador "+id+" no puede ser modificado al tener el atributo FINAL");
		}
		
		public static void compruebaFuncionID(String id) throws ErrorSemantico {
			Entorno top = entornoActual();
			Declaracion i = top.fullGetFuncion(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un sÃ­mbolo de funciÃ³n declarado en el entorno");
		}
		
		public static void compruebaClaseID(String id) throws ErrorSemantico {
			EntornoClase top = (EntornoClase) entornoActual();
			Declaracion i = top.fullGetClase(id);
			if(i == null)
				throw new ErrorSemantico("El id "+id+" no es un sÃ­mbolo de clase declarado en el entorno");
		}
		
		public static void entraBloqueClase(Declaracion identificadorClase) {
			EntornoClase e = new EntornoClase(entornoActual(), identificadorClase);
			pilaEntornos.push(e);
		}
		
		public static void entraBloqueIf() {
			Entorno e = new Entorno(entornoActual(), Tipo.IF);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueIf() throws IOException {
			Entorno popped = pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void entraBloqueElse() {
			Entorno e = new Entorno(entornoActual(), Tipo.ELSE);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueElse() throws IOException {
			Entorno popped = pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void entraBloqueWhile() {
			Entorno e = new Entorno(entornoActual(), Tipo.WHILE);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueWhile() throws IOException {
			Entorno popped = pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void saleBloqueClase() throws IOException {
			EntornoClase popped = (EntornoClase) pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void entraBloqueFuncion(Declaracion identificadorFuncion) {
			EntornoFuncion e = new EntornoFuncion((EntornoClase)entornoActual(), identificadorFuncion);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueFuncion(Boolean isBuiltIn) throws IOException {
			EntornoFuncion popped = (EntornoFuncion) pilaEntornos.pop();
			if(DEBUG_MODE && !isBuiltIn) {
				popped.printEntorno();
			}
		}
		
		public static Integer getIdentificador() {
			return CONTADOR++;
		}
		
		public static Integer getNodoIdentificadorUnico() {
			return _idnodoIncremental++;
		}
                
                public static DeclaracionConstante crearVariableTemporal(Tipo tipo, Object valor) throws ErrorSemantico {
                        return entornoActual().putConstante(tipo, null, valor);
                }
		
                public static Declaracion crearVariableTemporal(Tipo tipo) throws ErrorSemantico {
                        return entornoActual().put(tipo, null);
                }
                
                public static String generarEtiqueta() {
                    return "e" + SecuenciaIdEtiqueta++;
                }
	}