package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloAsignacion extends Nodo implements TipoSubyacente{
	
	private String tipo;
	private String id;
	private SimboloOperacion operacion;
	
	public SimboloAsignacion(String t, String i, SimboloOperacion o) {
		this.tipo = t;
		this.id = i;
		this.operacion = o;
	}
	
	public SimboloAsignacion(String t, String i) {
		this.tipo = t;
		this.id = i;
	}
	
	public SimboloAsignacion(String i, SimboloOperacion o) {
		this.id = i;
		this.operacion = o;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		
		if(tipo != null)
			hijos.add(new SimboloTerminal(tipo, Tipo.Identificador));
		
		hijos.add(new SimboloTerminal(this.id, Tipo.Identificador));
		
		if(operacion != null)
			hijos.add(operacion);
		
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PUNTOCOMA], Tipo.Token));
		
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloAsignacion";
	}
	
}
