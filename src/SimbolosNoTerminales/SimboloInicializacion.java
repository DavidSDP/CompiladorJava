package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloInicializacion extends Nodo implements TipoSubyacente{
	
	private SimboloOperacion simboloOperacion;
	
	public SimboloInicializacion() {
	}
	
	public SimboloInicializacion(SimboloOperacion simboloOperacion) {
		this.simboloOperacion = simboloOperacion;
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}

	public SimboloOperacion getSimboloOperacion() {
		return simboloOperacion;
	}
	
}
