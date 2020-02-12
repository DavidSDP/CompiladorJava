package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloClase extends Nodo implements TipoSubyacente {
	
	private String id;
	private SimboloCuerpo cuerpo;
	
	public SimboloClase(String i, SimboloCuerpo c) {
		this.id = i;
		this.cuerpo = c;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Class;
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(Tipo.Class.name(), Tipo.Identificador));
		hijos.add(new SimboloTerminal(id, Tipo.Identificador));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));
		if(cuerpo != null)
			hijos.add(cuerpo);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloClase";
	}
	
}
