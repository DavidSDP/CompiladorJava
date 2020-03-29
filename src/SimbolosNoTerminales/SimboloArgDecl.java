package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloArgDecl extends Nodo implements TipoSubyacente{
	
	private String id;
	private Tipo tipo;
	private SimboloArray simboloArrayDef;
	
	public SimboloArgDecl(String id, Tipo tipo, SimboloArray simboloArrayDef) {
		this.id = id;
		this.tipo = tipo;
		this.simboloArrayDef = simboloArrayDef;
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		if(this.simboloArrayDef != null)
			return Tipo.Array;
		return this.tipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public SimboloArray getSimboloArrayDef() {
		return simboloArrayDef;
	}

	public void setSimboloArrayDef(SimboloArray simboloArrayDef) {
		this.simboloArrayDef = simboloArrayDef;
	}
	
}
