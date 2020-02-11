package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloArgs implements TipoSubyacente {
	
	private String tipo;
	private String id;
	private SimboloArgs nextArg;
	
	public SimboloArgs(String t, String i, SimboloArgs n) {
		this.tipo = t;
		this.id = i;
		this.nextArg = n;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SimboloArgs getNextArg() {
		return nextArg;
	}

	public void setNextArg(SimboloArgs nextArg) {
		this.nextArg = nextArg;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
	
}
