package Procesador;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Checkers.Tipo;

public class Entorno {
	
	private Integer nivel;
	private Boolean esFuncion;
	
	// EXTRAPOLAR FUNCION-ENTORNO (HERENCIA)
	// CORREGIR SOBRECARGA DE CLASES -> IDENTIFICADOR concat ARGS...
	
	private Hashtable<String, Identificador> tablaIDs;
	private Hashtable<String, Identificador> tablaFunciones;
	private Hashtable<String, Identificador> tablaClases;
	
	// La tabla FuncionEntorno se declara en el mismo entorno donde la función ha sido declarada
	private Hashtable<String, Entorno> tablaFuncionEntorno;
	
	// Se guardan los Identificadores que son argumentos del Entorno (Sólo funciones)
	private List<String> listaArgumentos;
	
	private Entorno entornoAnterior;
	private Integer _identificador_entorno;
	
	public Entorno(Entorno entornoAnterior, Boolean esFuncion) {
		if(entornoAnterior == null) {
			this.nivel = 0;
		}else {
			this.nivel = entornoAnterior.getNivel() + 1;
		}
		this._identificador_entorno = GlobalVariables.getIdentificador();
		this.tablaIDs = new Hashtable<>();
		this.tablaFunciones = new Hashtable<>();
		this.tablaFuncionEntorno = new Hashtable<>();
		this.tablaClases = new Hashtable<>();
		this.entornoAnterior = entornoAnterior;
		this.esFuncion = esFuncion;
		if(esFuncion)
			this.listaArgumentos = new ArrayList<>();
	}

	////////*	IDENTIFICADORES		*////////
	
	// Introduce nuevo ID en el entorno actual
	public void put(Tipo tipo, String s) {
		if(this.contains(s))
			throw new Error("El identificador '"+s+"' se ha declarado por duplicado");
		this.tablaIDs.put(s, new Identificador(s, tipo));
	}
	
	// Devuelve true si el ID ha sido declarado en el entorno actual
	public Boolean contains(String s) {
		return this.tablaIDs.containsKey(s);
	}

	// Devuelve el ID especificado en el entorno actual
	public Identificador get(String s) {
		if(!this.contains(s)) {
			return null;
		}
		return this.tablaIDs.get(s);
	}
	
	// Devuelve el ID declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGet(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e.contains(s)) {
				return e.get(s);
			}
		}
		return null;
	}
	
	////////*	IDENTIFICADORES DE FUNCIONES	*////////

	// Introduce nuevo ID de Función en el entorno actual
	public void putFuncion(Tipo tipo, String s) {
		if(this.containsFuncion(s))
			throw new Error("El identificador de función '"+s+"' se ha declarado por duplicado");
		this.tablaFunciones.put(s, new Identificador(s, tipo));
	}
	
	// Devuelve true si el ID de Función ha sido declarado en el entorno actual
	public Boolean containsFuncion(String s) {
		return this.tablaFunciones.containsKey(s);
	}
	
	// Devuelve el ID de Función especificado en el entorno actual
	public Identificador getFuncion(String s) {
		if(!this.containsFuncion(s)) {
			return null;
		}
		return this.tablaFunciones.get(s);
	}
	
	// Devuelve el ID declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGetFuncion(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e.containsFuncion(s)) {
				return e.getFuncion(s);
			}
		}
		return null;
	}
	
	////////*	IDENTIFICADORES DE FUNCIONES/ARGUMENTOS		*////////
	
	// Especifica los argumentos de la función
	public void putFuncionArgs(String funcionID, String argumentoID) {
		if(!this.getEntornoAnterior().containsFuncion(funcionID))
			throw new Error("La función con identificador: '"+funcionID+"' no ha sido declarada en la tabla de funciones del entorno");
		
		if(!this.contains(argumentoID))
			throw new Error("El identificador: '"+argumentoID+"' no ha sido declarado en la tabla de identificadores del entorno");
		
		if(this.getListaArgumentos().contains(argumentoID))
			throw new Error("Se ha definido el argumento: '"+argumentoID+"' duplicado para la función '"+funcionID+"'");
		
		this.listaArgumentos.add(argumentoID);
	}
	
	// Devuelve true si el ID es un argumento de función especificada. Mismo entorno.
	public Boolean containsArgs(String argumentoID) {
		if(!this.listaArgumentos.contains(argumentoID))
			return false;
		return true;
	}
	
	// Devuelve la lista de Argumentos para la función especificada. Mismo entorno.
	public List<String> getArgs() {
		return this.listaArgumentos;
	}

	////////*	IDENTIFICADORES	DE CLASES	*////////
	
	// Introduce nuevo ID de Clase en el entorno actual
	public void putClase(String s) {
		if(this.contains(s))
			throw new Error("El identificador de clase '"+s+"' se ha declarado por duplicado");
		this.tablaClases.put(s, new Identificador(s, Tipo.Class));
	}
	
	// Devuelve true si el ID de Clase ha sido declarado en el entorno actual
	public Boolean containsClase(String s) {
		return this.tablaClases.containsKey(s);
	}

	// Devuelve el ID de Clase especificado en el entorno actual
	public Identificador getClase(String s) {
		if(!this.containsClase(s)) {
			return null;
		}
		return this.tablaClases.get(s);
	}
	
	// Devuelve el ID de Clase declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGetClase(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e.containsClase(s)) {
				return e.getClase(s);
			}
		}
		return null;
	}
	
	/* Dibujando el Entorno */
	
	public void printEntorno() {
		System.out.println();
		System.out.println(" -> ENTORNO "+this._identificador_entorno+", de nivel "+this.nivel+" <- ");
		System.out.println();
		
		System.out.println(" VARIABLES: ");
		if(tablaIDs.isEmpty()) {
			System.out.println(" - no hay identificadores declarados - ");
		}else {
			Iterator<String> iterator = tablaIDs.keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = tablaIDs.get(key);
				System.out.println("ID: "+id.getId()+" , TIPO: "+id.getTipo());
			}
		}

		System.out.println();
		System.out.println(" CLASES: ");
		if(tablaClases.isEmpty()) {
			System.out.println(" - no hay clases declaradas - ");
		}else {
			Iterator<String> iterator = tablaClases.keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = tablaClases.get(key);
				System.out.println("ID: "+id.getId()+" , TIPO: "+id.getTipo());
			}
		}
		
		System.out.println();
		System.out.println(" FUNCIONES: ");
		
		if(tablaFunciones.isEmpty()) {
			System.out.println(" - no hay funciones declaradas - ");
		}else {
			Iterator<String> iteratorFunciones = tablaFunciones.keySet().iterator();
			while(iteratorFunciones.hasNext()) {
				String key = (String) iteratorFunciones.next();
				Identificador funcionId = tablaFunciones.get(key);
				System.out.println("ID: "+funcionId.getId()+" , TIPO: "+funcionId.getTipo());
				System.out.println("     -> argumentos: ");
				List<String> argumentos = this.tablaFuncionEntorno.get(funcionId.getId()).getArgs();
				if(argumentos == null || argumentos.isEmpty()) {
					System.out.println("              -> sin argumentos <-");
				}else {
					for(String arg: argumentos) {
						Identificador idArgumento = this.tablaFuncionEntorno.get(funcionId.getId()).get(arg);
						System.out.println("             -> id: "+idArgumento.getId()+" , tipo: "+idArgumento.getTipo());
					}
				}
			}
		}
		System.out.println();
		System.out.println("_______________________________________");
		System.out.println();
	}
	
	
	public Integer getNivel() {
		return this.nivel;
	}

	public Entorno getEntornoAnterior() {
		return entornoAnterior;
	}

	public Hashtable<String, Identificador> getTablaIDs() {
		return tablaIDs;
	}

	public void setTablaIDs(Hashtable<String, Identificador> tablaIDs) {
		this.tablaIDs = tablaIDs;
	}

	public Hashtable<String, Identificador> getTablaFunciones() {
		return tablaFunciones;
	}

	public void setTablaFunciones(Hashtable<String, Identificador> tablaFunciones) {
		this.tablaFunciones = tablaFunciones;
	}

	public Hashtable<String, Entorno> getTablaFuncionEntorno() {
		return tablaFuncionEntorno;
	}

	public void setTablaFuncionEntorno(Hashtable<String, Entorno> tablaFuncionEntorno) {
		this.tablaFuncionEntorno = tablaFuncionEntorno;
	}

	public List<String> getListaArgumentos() {
		return listaArgumentos;
	}

	public void setListaArgumentos(List<String> listaArgumentos) {
		this.listaArgumentos = listaArgumentos;
	}
}
