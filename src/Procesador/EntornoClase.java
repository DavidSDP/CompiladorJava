package Procesador;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Checkers.Tipo;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

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
	public void putFuncion(Tipo tipo, String s) throws ErrorSemantico {
		if(this.containsFuncion(s))
			throw new ErrorSemantico("El identificador de función '"+s+"' se ha declarado por duplicado");
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
	public void putFuncionEntorno(String idFuncion, EntornoFuncion entornoFuncion) throws ErrorSemantico {
		if(this.containsFuncionEntorno(idFuncion))
			throw new ErrorSemantico("El entorno de la función '"+idFuncion+"' se ha declarado por duplicado");
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
	public void putClase(String s) throws ErrorSemantico {
		if(this.contains(s))
			throw new ErrorSemantico("El identificador de clase '"+s+"' se ha declarado por duplicado");
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
	
	public void printEntorno() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		if(this.getIdentificador() == null) {
			sb.append(" -> ENTORNO PROGRAMA PRINCIPAL "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		}else {
			sb.append(" -> ENTORNO CLASE "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		}
		sb.append("\n");

		sb.append("\n");
		sb.append(" VARIABLES: ");
		sb.append("\n");
		sb.append("\n");
		if(this.getTablaIDs().isEmpty()) {
			sb.append(" - no hay identificadores declarados - ");
			sb.append("\n");
		}else {
			Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = this.getTablaIDs().get(key);
				if(id.getEsConstante()) {
					sb.append("CONSTANTE "+"ID: "+id.getId()+" , TIPO: "+id.getTipo()+"");
				}else {
					sb.append("VARIABLE "+"ID: "+id.getId()+" , TIPO: "+id.getTipo()+"");
				}
				sb.append("\n");
				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append(" CLASES: ");
		sb.append("\n");
		sb.append("\n");
		if(tablaClases.isEmpty()) {
			sb.append(" - no hay clases declaradas - ");
			sb.append("\n");
			sb.append("\n");
		}else {
			Iterator<String> iterator = tablaClases.keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = tablaClases.get(key);
				sb.append("ID: "+id.getId()+" , TIPO: "+id.getTipo());
				sb.append("\n");
				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append(" FUNCIONES: ");
		sb.append("\n");
		sb.append("\n");
		
		if(tablaFunciones.isEmpty()) {
			sb.append("\n");
			sb.append(" - no hay funciones declaradas - ");
			sb.append("\n");
			sb.append("\n");
		}else {
			Iterator<String> iteratorFunciones = tablaFunciones.keySet().iterator();
			while(iteratorFunciones.hasNext()) {
				String key = (String) iteratorFunciones.next();
				Identificador funcionId = tablaFunciones.get(key);
				sb.append("ID: "+funcionId.getId()+" , TIPO: "+funcionId.getTipo());
				sb.append("\n");
				sb.append("     -> argumentos: ");
				sb.append("\n");
				sb.append("\n");
				List<String> argumentos = ((EntornoFuncion)this.tablaFuncionEntorno.get(funcionId.getId())).getArgs();
				if(argumentos == null || argumentos.isEmpty()) {
					sb.append("              -> sin argumentos <-");
					sb.append("\n");
					sb.append("\n");
				}else {
					for(String arg: argumentos) {
						Identificador idArgumento = this.tablaFuncionEntorno.get(funcionId.getId()).get(arg);
						sb.append("             -> id: "+idArgumento.getId()+" , tipo: "+idArgumento.getTipo());
						sb.append("\n");
						sb.append("\n");
					}
				}
			}
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("_______________________________________");
		sb.append("\n");
		sb.append("\n");
		FicheroEntornos.almacenaEntorno(sb.toString());
	}

}
