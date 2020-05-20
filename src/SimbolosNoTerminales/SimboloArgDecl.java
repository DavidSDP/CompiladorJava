package SimbolosNoTerminales;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloArgDecl extends Nodo implements TipoSubyacente{
	
	private String id;
	private TipoObject tipo;
	private SimboloArray simboloArrayDef;
	
	public SimboloArgDecl(String id, TipoObject tipo, SimboloArray simboloArrayDef) {
		this.id = id;
		this.tipo = tipo;
		this.simboloArrayDef = simboloArrayDef;
	}
	
	@Override
	public TipoObject getTipoSubyacente() {
		if(this.simboloArrayDef != null)
			return Tipo.getTipoSafe(Tipo.Array);
		return this.tipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TipoObject getTipo() {
		return tipo;
	}

	public void setTipo(TipoObject tipo) {
		this.tipo = tipo;
	}

	public SimboloArray getSimboloArrayDef() {
		return simboloArrayDef;
	}

	public void setSimboloArrayDef(SimboloArray simboloArrayDef) {
		this.simboloArrayDef = simboloArrayDef;
	}
	
}
