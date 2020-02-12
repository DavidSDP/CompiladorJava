package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;

public class SimboloPrograma extends Nodo implements TipoSubyacente{
	
	private SimboloClase clase;
	
	public SimboloPrograma(SimboloClase c) {
		this.clase = c;
	}

	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(this.clase);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloPrograma";
	}
	
}
