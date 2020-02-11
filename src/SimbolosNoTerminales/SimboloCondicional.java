package SimbolosNoTerminales;

import java.util.List;

import Procesador.Tipo;

public class SimboloCondicional implements TipoSubyacente, Nodo{
	
	private SimboloOperacion operacion;
	private SimboloContenido contenido;
	private SimboloCondElse contenidoElse;
	
	public SimboloCondicional(SimboloOperacion operacion, SimboloContenido contenido) {
		this.operacion = operacion;
		this.contenido = contenido;
	}
	
	public SimboloCondicional(SimboloOperacion operacion, SimboloContenido contenido,
			SimboloCondElse contenidoElse) {
		this.operacion = operacion;
		this.contenido = contenido;
		this.contenidoElse = contenidoElse;
	}

	@Override
	public Nodo getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Nodo> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}
	
}
