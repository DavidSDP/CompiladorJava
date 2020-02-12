package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.GlobalVariables;
import Procesador.Identificador;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloFuncionInvk extends Nodo implements TipoSubyacente{
	
	private String idFuncion;
	private SimboloParams params;
	
	public SimboloFuncionInvk(String i, SimboloParams p) {
		this.idFuncion = i;
		this.params = p;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(idFuncion, Tipo.Identificador));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
		if(params != null)
			hijos.add(params);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloFuncionInvk";
	}

	@Override
	public Tipo getTipoSubyacente() {
		Identificador identificadorFuncion = GlobalVariables.entornoActual().fullGetFuncion(idFuncion);
		return identificadorFuncion.getTipo();
	}
	
}
