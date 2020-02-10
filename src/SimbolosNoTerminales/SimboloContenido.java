package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloContenido implements TipoSubyacente{
	
	private SimboloContenido contenido;
	private SimboloExpresion expresion;
	private SimboloOperacion operacion;
	private SimboloCondicional condicional;
	private String retorno;
	
	public SimboloContenido(SimboloContenido c, SimboloExpresion e) {
		this.contenido = c;
		this.expresion = e;
	}
	
	public SimboloContenido(SimboloContenido c, SimboloCondicional d) {
		this.contenido = c;
		this.condicional = d;
	}
	
	public SimboloContenido(SimboloContenido c, String r, SimboloOperacion o) {
		this.retorno = r;
		this.contenido = c;
		this.operacion = o;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
