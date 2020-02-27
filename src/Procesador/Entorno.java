package Procesador;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import Checkers.Tipo;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class Entorno {
	
	private Integer nivel;
	private Identificador identificador;
	private Integer _identificador_entorno;
	
	private Hashtable<String, Identificador> tablaIDs;
	private Entorno entornoAnterior;
	
	public Entorno(Entorno entornoAnterior, Tipo tipo) {
		if(entornoAnterior == null) {
			this.setNivel(0);
		}else {
			this.setNivel(entornoAnterior.getNivel() + 1);
		}
		this.set_identificador_entorno(GlobalVariables.getIdentificador());
		this.identificador = new Identificador(tipo.name(), tipo);
		this.tablaIDs = new Hashtable<>();
		this.entornoAnterior = entornoAnterior;
	}
	
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
	public void put(Tipo tipo, String s) throws ErrorSemantico {
		if(this.contains(s))
			throw new ErrorSemantico("El identificador '"+s+"' se ha declarado por duplicado");
		this.tablaIDs.put(s, new Identificador(s, tipo));
	}
	
	// Introduce nuevo ID constante en el entorno actual
	public void put(Tipo tipo, String s, Boolean esConstante) throws ErrorSemantico {
		if(this.contains(s))
			throw new ErrorSemantico("El identificador '"+s+"' se ha declarado por duplicado");
		Identificador nuevoIdentificador = new Identificador(s, tipo, esConstante);
		this.tablaIDs.put(s, nuevoIdentificador);
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
	
	// Devuelve el ID declarado m�s cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGet(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e.contains(s)) {
				return e.get(s);
			}
		}
		return null;
	}
	
	// Devuelve el ID  de Funci�n declarado m�s cercano (hacia arriba por entornos), null si no ha sido declarado
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
	
	// Devuelve el Entorno de Funci�n declarado m�s cercano (hacia arriba por entornos), null si no ha sido declarado
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
	
	// Devuelve el ID de Clase declarado m�s cercano (hacia arriba por entornos), null si no ha sido declarado
	public Identificador fullGetClase(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(((EntornoClase)e).containsClase(s)) {
				return ((EntornoClase)e).getClase(s);
			}
		}
		return null;
	}
	
	/* Dibujando el Entorno */
	
	public void printEntorno() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(" -> ENTORNO TIPO: "+this.getIdentificador().getTipo()+" "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		sb.append("\n");

		sb.append("\n");
		sb.append(" VARIABLES: ");
		sb.append("\n");
		sb.append("\n");
		if(this.getTablaIDs().isEmpty()) {
			sb.append("\n");
			sb.append(" - no hay identificadores declarados - ");
			sb.append("\n");
			sb.append("\n");
		}else {
			Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = this.getTablaIDs().get(key);
				sb.append("\n");
				sb.append("\n");
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
		sb.append("_______________________________________");
		sb.append("\n");
		FicheroEntornos.almacenaEntorno(sb.toString());
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
