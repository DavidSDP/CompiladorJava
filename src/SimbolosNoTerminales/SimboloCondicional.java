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

public class SimboloCondicional extends Nodo implements TipoSubyacente{
	
	private SimboloOperacion operacion;
	private SimboloContenido contenido;
	private SimboloCondElse contenidoElse;
	
	public SimboloCondicional(SimboloOperacion operacion, SimboloContenido contenido) {
		this.operacion = operacion;
		this.contenido = contenido;
	}
	
	public SimboloCondicional(SimboloOperacion operacion, SimboloContenido contenido,
			SimboloCondElse contenidoElse) {
		this.operacion = operacion;
		this.contenido = contenido;
		this.contenidoElse = contenidoElse;
	}
        
        public SimboloCondicional(SimboloCondicionalBloquePrincipal ppal, 
                SimboloCondElse contenidoElse) {
            this.operacion = ppal.getClausula().getOperacion();
            this.contenido = ppal.getContenido();
            this.contenidoElse = contenidoElse;
        }

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.TIF], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
		hijos.add(operacion);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));
		if(contenido != null)
			hijos.add(contenido);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		if(contenidoElse != null)
			hijos.add(contenidoElse);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloCondicional";
	}

	@Override
	public TipoObject getTipoSubyacente() {
		return Tipo.getTipoSafe(Tipo.Void);
	}
	
}
