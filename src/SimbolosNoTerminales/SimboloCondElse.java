package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloCondElse implements TipoSubyacente{
	
	private SimboloContenido contenido;
	
	public SimboloCondElse(SimboloContenido c) {
		this.contenido = c;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
