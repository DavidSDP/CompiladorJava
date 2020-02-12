package Procesador;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Checkers.Tipo;

public class EntornoClase extends Entorno{
	
	private Hashtable<String, Identificador> tablaFunciones;
	private Hashtable<String, Identificador> tablaClases;
	
	// La tabla FuncionEntorno se declara en el mismo entorno donde la función ha sido declarada
	private Hashtable<String, EntornoFuncion> tablaFuncionEntorno;

	public EntornoClase(Entorno entornoAnterior, Identificador identificador) {
		super(entornoAnterior, identificador);
		this.tablaFunciones = new Hashtable<>();
		this.tablaClases = new Hashtable<>();
		this.tablaFuncionEntorno = new Hashtable<>();
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
	////////*	FUNCION - ENTORNO	*////////
	
	// Introduce nuevo ID de Función en el entorno actual
	public void putFuncionEntorno(String idFuncion, EntornoFuncion entornoFuncion) {
		if(this.containsFuncionEntorno(idFuncion))
			throw new Error("El entorno de la función '"+idFuncion+"' se ha declarado por duplicado");
		this.tablaFuncionEntorno.put(idFuncion, entornoFuncion);
	}
	
	// Devuelve true si el ID de Función ha sido declarado en el entorno actual
	public Boolean containsFuncionEntorno(String s) {
		return this.tablaFuncionEntorno.containsKey(s);
	}
	
	// Devuelve el ID de Función especificado en el entorno actual
	public EntornoFuncion getFuncionEntorno(String s) {
		if(!this.containsFuncionEntorno(s)) {
			return null;
		}
		return this.tablaFuncionEntorno.get(s);
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
	
	/* Dibujando el Entorno */
	
	public void printEntorno() {
		System.out.println();
		System.out.println(" -> ENTORNO CLASE "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		System.out.println();
		
		System.out.println(" VARIABLES: ");
		if(this.getTablaIDs().isEmpty()) {
			System.out.println(" - no hay identificadores declarados - ");
		}else {
			Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = this.getTablaIDs().get(key);
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
				List<String> argumentos = ((EntornoFuncion)this.tablaFuncionEntorno.get(funcionId.getId())).getArgs();
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

}
