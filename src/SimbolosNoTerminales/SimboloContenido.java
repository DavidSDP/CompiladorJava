package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloContenido extends Nodo implements TipoSubyacente{
	
	private SimboloContenido contenido;
	private SimboloExpresion expresion;
	private SimboloOperacion operacion;
	private SimboloCondicional condicional;
	private SimboloBucle bucle;
	private String retorno;
	
	public SimboloContenido(SimboloContenido c, SimboloExpresion e) {
		this.contenido = c;
		this.expresion = e;
	}
	
	public SimboloContenido(SimboloContenido c, SimboloCondicional d) {
		this.contenido = c;
		this.condicional = d;
	}
	
	public SimboloContenido(SimboloContenido c, SimboloBucle b) {
		this.contenido = c;
		this.bucle = b;
	}
	
	public SimboloContenido(SimboloContenido c, String r, SimboloOperacion o) {
		this.retorno = r;
		this.contenido = c;
		this.operacion = o;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		
		if(contenido != null)
			hijos.add(contenido);
		
		if(expresion != null) {
			hijos.add(expresion);
			return hijos;
		}
		
		if(condicional != null) {
			hijos.add(condicional);
			return hijos;
		}
		
		if(bucle != null) {
			hijos.add(bucle);
			return hijos;
		}
		
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.TRETURN], Tipo.Token));
		hijos.add(operacion);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PUNTOCOMA], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloContenido";
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
