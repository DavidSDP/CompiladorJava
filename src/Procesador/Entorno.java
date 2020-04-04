package Procesador;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import Checkers.Tipo;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class Entorno {
	
	private Integer nivel;
	
	private Declaracion identificador;
	
	private Hashtable<String, Declaracion> tablaIDs;
	
	private Entorno entornoAnterior;
	private Integer _identificador_entorno;
	
	public Entorno(Entorno entornoAnterior, Tipo tipo) {
		if(entornoAnterior == null) {
			this.setNivel(0);
		}else {
			this.setNivel(entornoAnterior.getNivel() + 1);
		}
		this.set_identificador_entorno(GlobalVariables.getIdentificador());
		this.identificador = new Declaracion(new Identificador(tipo.name(), tipo.name()), tipo);
		this.tablaIDs = new Hashtable<>();
		this.entornoAnterior = entornoAnterior;
	}
	
	public Entorno(Entorno entornoAnterior, Declaracion identificador) {
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
	
	public Declaracion getIdentificadorFuncionRetorno() {
		EntornoFuncion entornoFuncionSuperior = (EntornoFuncion) getEntornoFuncionSuperior(this);
		if(entornoFuncionSuperior == null)
			return null;
		return entornoFuncionSuperior.getIdentificador();
	}
	
	private static Entorno getEntornoFuncionSuperior(Entorno entorno) {
		if(entorno == null)
			return null;
		if(entorno instanceof EntornoFuncion)
			return entorno;
		return getEntornoFuncionSuperior(entorno.getEntornoAnterior());
	}

	////////*	IDENTIFICADORES		*////////
	
	// Introduce nuevo ID en el entorno actual
	public Declaracion put(Tipo tipo, String s) throws ErrorSemantico {
		if(this.contains(s))
			throw new ErrorSemantico("El identificador '"+s+"' se ha declarado por duplicado");
                Declaracion nuevaDeclaracion = new Declaracion(new Identificador(s, s), tipo);
		this.tablaIDs.put(s, nuevaDeclaracion);
                return nuevaDeclaracion;
	}
	
	// Introduce nuevo ID constante en el entorno actual
	public DeclaracionConstante putConstante(Tipo tipo, String s, Object valor) throws ErrorSemantico {
		if(this.contains(s))
			throw new ErrorSemantico("La constante '"+s+"' se ha declarado por duplicado");
                
                DeclaracionConstante nuevoIdentificador = new DeclaracionConstante(new Identificador(s, s), tipo, valor);
		this.tablaIDs.put(s, nuevoIdentificador);
                return nuevoIdentificador;
	}
	
	// Devuelve true si el ID ha sido declarado en el entorno actual
	public Boolean contains(String s) {
		return this.tablaIDs.containsKey(s);
	}

	// Devuelve el ID especificado en el entorno actual
	public Declaracion get(String s) {
		if(!this.contains(s)) {
			return null;
		}
		return this.tablaIDs.get(s);
	}
	
	// Devuelve el ID declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Declaracion fullGet(String s) {
		for(Entorno e = this; e != null; e = e.getEntornoAnterior()) {
			if(e.contains(s)) {
				return e.get(s);
			}
		}
		return null;
	}
	
	// Devuelve el ID  de Función declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
	public Declaracion fullGetFuncion(String s) {
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
	public Declaracion fullGetClase(String s) {
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
				Declaracion id = this.getTablaIDs().get(key);
				sb.append("\n");
				sb.append("\n");
				if(id instanceof DeclaracionConstante) {
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

	public Hashtable<String, Declaracion> getTablaIDs() {
		return tablaIDs;
	}

	public void setTablaIDs(Hashtable<String, Declaracion> tablaIDs) {
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

	public Declaracion getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Declaracion identificador) {
		this.identificador = identificador;
	}
}
