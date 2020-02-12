package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloArgs extends Nodo implements TipoSubyacente {
	
	private String tipo;
	private String id;
	private SimboloArgs nextArg;
	private Boolean esPrimeraProduccion;
	
	public SimboloArgs(String t, String i, SimboloArgs n, Boolean esPrimeraProduccion) {
		this.tipo = t;
		this.id = i;
		this.nextArg = n;
		this.esPrimeraProduccion = esPrimeraProduccion;
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

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		
		if(!esPrimeraProduccion)
			hijos.add(new SimboloTerminal(sym.terminalNames[sym.COMA], Tipo.Token));
		
		hijos.add(new SimboloTerminal(tipo, Tipo.Identificador));
		hijos.add(new SimboloTerminal(id, Tipo.Identificador));
		if(nextArg != null)
			hijos.add(nextArg);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloArgs";
	}
	
	
}
