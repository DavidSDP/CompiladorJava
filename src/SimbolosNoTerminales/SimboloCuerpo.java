package SimbolosNoTerminales;

import Procesador.Tipo;

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
