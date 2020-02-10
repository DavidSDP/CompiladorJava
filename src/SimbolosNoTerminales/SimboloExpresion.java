package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloExpresion implements TipoSubyacente{
	
	private SimboloAsignacion a;
	private SimboloFuncionInvk f;
	
	public SimboloExpresion(SimboloAsignacion a) {
		this.a = a;
	}
	
	public SimboloExpresion(SimboloFuncionInvk f) {
		this.f = f;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
