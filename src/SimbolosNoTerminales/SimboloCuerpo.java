package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloCuerpo implements TipoSubyacente{
	
	private SimboloCuerpo cuerpo;
	private SimboloElemento elemento;
	
	public SimboloCuerpo(SimboloCuerpo c, SimboloElemento e) {
		this.cuerpo = c;
		this.elemento = e;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
