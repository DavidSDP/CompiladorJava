package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloElemento implements TipoSubyacente{
	
	private SimboloAsignacion asignacion;
	private SimboloFuncionDecl funcionDecl;
	private SimboloClase clase;
	
	public SimboloElemento(SimboloAsignacion a) {
		this.asignacion = a;
	}
	
	public SimboloElemento(SimboloFuncionDecl f) {
		this.funcionDecl = f;
	}
	
	public SimboloElemento(SimboloClase c) {
		this.clase = c;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
}
