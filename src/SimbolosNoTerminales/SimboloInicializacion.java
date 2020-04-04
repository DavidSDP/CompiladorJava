package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.Declaracion;
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
                if (simboloOperacion != null)
                    return simboloOperacion.getTipoSubyacente();
                else
                    return Tipo.Void;
	}

	public SimboloOperacion getSimboloOperacion() {
		return simboloOperacion;
	}
	
        public Declaracion getDeclaracionOperacion() {
                if (simboloOperacion != null)
                    return simboloOperacion.getDeclaracion();
                else 
                    return null; 
        }
}
