package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

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
		if(clase!=null)
			return Tipo.Class;
		return Tipo.Void;
	}
}
