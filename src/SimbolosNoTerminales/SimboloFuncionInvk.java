package SimbolosNoTerminales;

import Procesador.GlobalVariables;
import Procesador.Identificador;
import Procesador.Tipo;

public class SimboloFuncionInvk implements TipoSubyacente{
	
	private String idFuncion;
	private SimboloParams params;
	
	public SimboloFuncionInvk(String i, SimboloParams p) {
		this.idFuncion = i;
		this.params = p;
	}

	@Override
	public Tipo getTipoSubyacente() {
		Identificador identificadorFuncion = GlobalVariables.entornoActual().fullGetFuncion(idFuncion);
		return identificadorFuncion.getTipo();
	}
	
}
