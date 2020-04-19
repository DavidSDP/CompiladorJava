package Procesador;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class EntornoClase extends Entorno {
	private static int classSequence = 0;
	
	private Hashtable<String, DeclaracionFuncion> tablaFunciones;
	private Hashtable<String, Declaracion> tablaClases;
	
	// La tabla FuncionEntorno se declara en el mismo entorno donde la función ha sido declarada
	private Hashtable<String, EntornoFuncion> tablaFuncionEntorno;

	private int profundidad;

	public EntornoClase(Entorno entornoPadre, Declaracion identificador) {
		super(entornoPadre, identificador);
		this.tablaFunciones = new Hashtable<>();
		this.tablaClases = new Hashtable<>();
		this.tablaFuncionEntorno = new Hashtable<>();

		// Notese que este es el único punto donde se puede crear un entorno sin entorno padre.
		// Tal como esta montada la gramatica, las funciones no pueden estar fuera de las clases.
		if (entornoPadre == null) {
			this.profundidad = 0;
		} else {
			this.profundidad = entornoPadre.getProfundidad() + 1;
		}
	}
	
	////////*	IDENTIFICADORES DE FUNCIONES	*////////

	// Introduce nuevo ID de Función en el entorno actual
	public DeclaracionFuncion putFuncion(TipoObject tipo, String s, String etiqueta) throws ErrorSemantico {
		if(this.containsFuncion(s))
			throw new ErrorSemantico("El identificador de función '"+s+"' se ha declarado por duplicado");

		DeclaracionFuncion decl = new DeclaracionFuncion(new Identificador(s, s), tipo, etiqueta);
		this.tablaFunciones.put(s, decl);
		return decl;
	}
	
	// Devuelve true si el ID de Función ha sido declarado en el entorno actual
	public Boolean containsFuncion(String s) {
		return this.tablaFunciones.containsKey(s);
	}
	
	// Devuelve el ID de Función especificado en el entorno actual
	public DeclaracionFuncion getFuncion(String s) {
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
	public DeclaracionClase putClase(String s) throws ErrorSemantico {
		if(this.containsSoloPropioEntorno(s))
			throw new ErrorSemantico("El identificador de clase '"+s+"' se ha declarado por duplicado");
		DeclaracionClase decl = new DeclaracionClase(new Identificador(s, s), Tipo.getTipo(Tipo.Class.name().toLowerCase()));
		decl.setEtiquetaDeclaraciones(generateClassLabel());
		decl.setEtiquetaPostInicializacion(generateClassLabel());
		this.tablaClases.put(s, decl);
		return decl;
	}
	
	// Devuelve true si el ID de Clase ha sido declarado en el entorno actual
	public Boolean containsClase(String s) {
		return this.tablaClases.containsKey(s);
	}

	// Devuelve el ID de Clase especificado en el entorno actual
	public Declaracion getClase(String s) {
		if(!this.containsClase(s)) {
			return null;
		}
		return this.tablaClases.get(s);
	}

	@Override
	public int getProfundidad() {
		return this.profundidad;
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
				Declaracion id = this.getTablaIDs().get(key);
				if(id instanceof DeclaracionConstante) {
					sb.append("CONSTANTE "+"ID: "+id.getId().getId()+" , TIPO: "+id.getTipo()+"");
				}else {
					sb.append("VARIABLE "+"ID: "+id.getId().getId()+" , TIPO: "+id.getTipo()+"");
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
				Declaracion id = tablaClases.get(key);
				sb.append("ID: "+id.getId().getId()+" , TIPO: "+id.getTipo());
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
				Declaracion funcionId = tablaFunciones.get(key);
				sb.append("ID: "+funcionId.getId().getId()+" , TIPO: "+funcionId.getTipo());
				sb.append("\n");
				sb.append("     -> argumentos: ");
				sb.append("\n");
				sb.append("\n");
				List<String> argumentos = ((EntornoFuncion)this.tablaFuncionEntorno.get(funcionId.getId().getId())).getArgs();
				if(argumentos == null || argumentos.isEmpty()) {
					sb.append("              -> sin argumentos <-");
					sb.append("\n");
					sb.append("\n");
				}else {
					for(String arg: argumentos) {
						Declaracion idArgumento = this.tablaFuncionEntorno.get(funcionId.getId().getId()).get(arg);
						sb.append("             -> id: "+idArgumento.getId().getId()+" , tipo: "+idArgumento.getTipo());
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

	protected String generateClassLabel() {
		return "c" + ++classSequence;
	}

	/**
	 * La memoria necesaria para una clase es solo la acumlacion
	 * del tamano de las variables.
	 *
	 * Las funciones no utilizan ningun sistema que necesite almacenar la memoria en runtime ( vtable )
	 *
	 */
	public int getTamanoMemoriaNecesaria() {
		int tamano = 0;
		for (Declaracion decl: ids) {
			tamano += decl.getOcupacion();
		}
		return tamano;
	}
}
