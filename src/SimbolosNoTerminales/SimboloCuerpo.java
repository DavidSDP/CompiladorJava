package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;

public class SimboloCuerpo extends Nodo implements TipoSubyacente {
	
	private SimboloCuerpo cuerpo;
	private SimboloElemento elemento;
	
	public SimboloCuerpo(SimboloCuerpo c, SimboloElemento e) {
		this.cuerpo = c;
		this.elemento = e;
	}

	@Override
	public TipoObject getTipoSubyacente() {
		return Tipo.getTipoSafe(Tipo.Void);
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		if(cuerpo != null)
			hijos.add(cuerpo);
		if(elemento != null)
			hijos.add(elemento);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloCuerpo";
	}
	
}
