package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloBucle extends Nodo implements TipoSubyacente{
	
        private SimboloCondicionBucle operacion;
	private SimboloContenido contenido;
	
	public SimboloBucle(SimboloCondicionBucle operacion, SimboloContenido contenido) {
            this.operacion = operacion;
            this.contenido = contenido;
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.TWHILE], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
		hijos.add(operacion);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));
		if(contenido != null)
			hijos.add(contenido);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloBucle";
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
}
