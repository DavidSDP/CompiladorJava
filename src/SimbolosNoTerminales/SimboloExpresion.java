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
		if(this.a != null)
			return this.a.getTipoSubyacente();
		if(this.f != null)
			return this.f.getTipoSubyacente();
		return null;
	}
	
}
