package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloFuncionDecl implements TipoSubyacente{
	
	private String id;
	private String tipo;
	
	public SimboloFuncionDecl(String i, String t) {
		this.id = i;
		this.tipo = t;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
