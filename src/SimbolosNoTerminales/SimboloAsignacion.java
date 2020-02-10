package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloAsignacion implements TipoSubyacente{
	
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
	
}
