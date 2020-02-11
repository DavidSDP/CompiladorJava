package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloParams implements TipoSubyacente{
	
	private SimboloOperacion operacion;
	private SimboloParams nextParam;
	
	public SimboloParams(SimboloOperacion o, SimboloParams n) {
		this.setOperacion(o);
		this.setNextParam(n);
	}

	public SimboloOperacion getOperacion() {
		return operacion;
	}

	public void setOperacion(SimboloOperacion operacion) {
		this.operacion = operacion;
	}

	public SimboloParams getNextParam() {
		return nextParam;
	}

	public void setNextParam(SimboloParams nextParam) {
		this.nextParam = nextParam;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
}
