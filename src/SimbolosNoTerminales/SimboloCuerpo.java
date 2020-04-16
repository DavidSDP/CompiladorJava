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
	private SimboloFuncionDecl funcion;
	
	public SimboloCuerpo(SimboloCuerpo c, SimboloFuncionDecl e) {
		this.cuerpo = c;
		this.funcion = e;
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
		if(funcion != null)
			hijos.add(funcion);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloCuerpo";
	}
	
}
