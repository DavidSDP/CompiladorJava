package Procesador;

import java.util.Hashtable;

import Checkers.Tipo;

public class Entorno {
	
	private Integer nivel;
	
	// EXTRAPOLAR FUNCION-ENTORNO (HERENCIA)
	// CORREGIR SOBRECARGA DE CLASES -> IDENTIFICADOR concat ARGS...
	
	private Identificador identificador;
	
	private Hashtable<String, Identificador> tablaIDs;
	
	private Entorno entornoAnterior;
	private Integer _identificador_entorno;
	
	public Entorno(Entorno entornoAnterior, Identificador identificador) {
		if(entornoAnterior == null) {
			this.setNivel(0);
		}else {
			this.setNivel(entornoAnterior.getNivel() + 1);
		}
		this.set_identificador_entorno(GlobalVariables.getIdentificador());
		this.identificador = identificador;
		this.tablaIDs = new Hashtable<>();
		this.entornoAnterior = entornoAnterior;
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
	
	// Devuelve el ID  de Función declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGetFuncion(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e instanceof EntornoClase) {
				if(((EntornoClase)e).containsFuncion(s)) {
					return ((EntornoClase)e).getFuncion(s);
				}
			}
		}
		return null;
	}
	
	// Devuelve el Entorno de Función declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public EntornoFuncion fullGetFuncionEntorno(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e instanceof EntornoClase) {
				if(((EntornoClase)e).containsFuncion(s)) {
					return ((EntornoClase)e).getFuncionEntorno(s);
				}
			}
		}
		return null;
	}
	
	// Devuelve el ID de Clase declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGetClase(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(((EntornoClase)e).containsClase(s)) {
				return ((EntornoClase)e).getClase(s);
			}
		}
		return null;
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

	public Integer get_identificador_entorno() {
		return _identificador_entorno;
	}

	public void set_identificador_entorno(Integer _identificador_entorno) {
		this._identificador_entorno = _identificador_entorno;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public Identificador getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Identificador identificador) {
		this.identificador = identificador;
	}
}
