package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloExpresion extends Nodo implements TipoSubyacente{
	
	private SimboloAsignacion a;
	private SimboloFuncionInvk f;
	private SimboloDeclaracion d;
	
	public SimboloExpresion(SimboloAsignacion a) {
		this.a = a;
	}
	
	public SimboloExpresion(SimboloFuncionInvk f) {
		this.f = f;
	}
	
	public SimboloExpresion(SimboloDeclaracion d) {
		this.d = d;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		if(a != null) {
			hijos.add(a);
			return hijos;
		}
		if(f != null) {
			hijos.add(f);
			return hijos;
		}
		hijos.add(d);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PUNTOCOMA], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloExpresion";
	}
	
	@Override
	public TipoObject getTipoSubyacente() {
		if(this.a != null)
			return this.a.getTipoSubyacente();
		if(this.f != null)
			return this.f.getTipoSubyacente();
		if(this.d != null)
			return this.d.getTipoSubyacente();
		return null;
	}
	
}
