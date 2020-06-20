package analisisSintactico.arbol;

import java.util.List;

import Checkers.Tipo;

public class SimboloTerminal extends Nodo{

	private String id;
	private Tipo tipo;
	
	public SimboloTerminal(String id, Tipo tipo) {
		this.id = id;
		this.setTipo(tipo);
	}

	public SimboloTerminal(Object valor, Tipo tipo) {
		this.id = valor.toString();
		this.setTipo(tipo);
	}
	
	@Override
	public List<INodo> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return id+" ("+this.tipo+")";
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

}
