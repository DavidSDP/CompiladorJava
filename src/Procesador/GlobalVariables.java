package Procesador;

import java.util.EmptyStackException;
import java.util.Stack;

import SimbolosNoTerminales.SimboloArgs;

public class GlobalVariables{
		
		public static final String FICHERO_TOKENS = "./output/FicheroTokens.txt";
		public static Boolean DEBUG_MODE = true;
		static Integer CONTADOR = 1;
		private static Stack<Entorno> pilaEntornos = new Stack<>();
		
		public static Entorno entornoActual() {
			try {
				return pilaEntornos.peek();
			}catch(EmptyStackException e) {
				return null;
			}
		}
		
		public static void asignaID(String id, String tipo) {
			Entorno top = entornoActual();
			top.put(Tipo.getTipo(tipo), id);
		}
		
		public static void asignaID(String id, Tipo tipo) {
			Entorno top = entornoActual();
			top.put(tipo, id);
		}
		
		public static void asignaFuncionID(String idFuncion, String tipo) {
			Entorno top = entornoActual();
			top.putFuncion(Tipo.getTipo(tipo), idFuncion);
		}
		
		public static void asignaFuncionID(String idFuncion, Tipo tipo) {
			Entorno top = entornoActual();
			top.putFuncion(tipo, idFuncion);
		}
		
		// Llamar una vez dentro del entorno de la función
		public static void asignaEntornoFuncionID(String idFuncion) {
			Entorno top = entornoActual();
			top.getEntornoAnterior().getTablaFuncionEntorno().put(idFuncion, top);
		}
		
		public static void asignaFuncionArgs(String idFuncion, SimboloArgs args) {
			Entorno top = entornoActual();
			for(SimboloArgs a = args; a != null; a = a.getNextArg()) {
				top.putFuncionArgs(idFuncion, a.getId());
			}
		}
		
		public static void asignaClaseID(String id) {
			Entorno top = entornoActual();
			top.putClase(id);
		}
		
		public static void compruebaID(String id) {
			Entorno top = entornoActual();
			Identificador i = top.fullGet(id);
			if(i == null)
				throw new Error("El id "+id+" no es un símbolo declarado en el entorno");
		}
		
		public static void compruebaFuncionID(String id) {
			Entorno top = entornoActual();
			Identificador i = top.fullGetFuncion(id);
			if(i == null)
				throw new Error("El id "+id+" no es un símbolo de función declarado en el entorno");
		}
		
		public static void compruebaClaseID(String id) {
			Entorno top = entornoActual();
			Identificador i = top.fullGetClase(id);
			if(i == null)
				throw new Error("El id "+id+" no es un símbolo de clase declarado en el entorno");
		}
		
		public static void entraBloque() {
			Entorno e = new Entorno(entornoActual(), false);
			pilaEntornos.push(e);
		}
		
		public static void saleBloque() {
			Entorno popped = pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static void entraBloqueFuncion() {
			Entorno e = new Entorno(entornoActual(), true);
			pilaEntornos.push(e);
		}
		
		public static void saleBloqueFuncion() {
			Entorno popped = pilaEntornos.pop();
			if(DEBUG_MODE) {
				popped.printEntorno();
			}
		}
		
		public static Integer getIdentificador() {
			return CONTADOR++;
		}
		
	}