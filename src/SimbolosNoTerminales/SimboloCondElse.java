package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloCondElse extends Nodo implements TipoSubyacente{
	
	private SimboloContenido contenido;
	
	public SimboloCondElse(SimboloContenido c) {
		this.contenido = c;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.TELSE], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));
		if(contenido != null)
			hijos.add(contenido);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloCondElse";
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
