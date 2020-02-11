package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;

public class SimboloFuncionDecl implements TipoSubyacente{
	
	private String id;
	private Tipo tipo;
	
	public SimboloFuncionDecl(String i, Tipo t) {
		this.id = i;
		this.tipo = t;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
