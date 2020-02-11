package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloPrograma implements TipoSubyacente{
	
	private SimboloClase clase;
	
	public SimboloPrograma(SimboloClase c) {
		this.clase = c;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
