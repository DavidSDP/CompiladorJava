package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.Declaracion;
import Procesador.DeclaracionFuncion;
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
	private DeclaracionFuncion decl;

	public SimboloFuncionDecl(DeclaracionFuncion decl, SimboloArgs a, SimboloContenido c) {
		this.id = decl.getId().getId();
		this.tipo = decl.getTipo();
		this.a = a;
		this.c = c;
		this.decl = decl;
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

	public String getId() {
		return this.id;
	}

	public void setArgs(SimboloArgs a) {
		this.a = a;
	}

	public void setContenido(SimboloContenido c) {
		this.c = c;
	}

	public DeclaracionFuncion getDeclaracion() {
		return this.decl;
	}
	
}
