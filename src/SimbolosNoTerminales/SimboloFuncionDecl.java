package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloFuncionDecl extends Nodo implements TipoSubyacente{
	
	private String id;
	private Tipo tipo;
	private SimboloArgs a;
	private SimboloContenido c;
	
	public SimboloFuncionDecl(String i, Tipo t, SimboloArgs a, SimboloContenido c) {
		this.id = i;
		this.tipo = t;
		this.a = a;
		this.c = c;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(tipo.name(), Tipo.Identificador));
		hijos.add(new SimboloTerminal(id, Tipo.Identificador));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
		if(a != null)
			hijos.add(a);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));
		if(c != null)
			hijos.add(c);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloFuncionDecl";
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
