package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.DeclaracionClase;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloClase extends Nodo implements TipoSubyacente {

	private DeclaracionClase decl;
	private SimboloDeclaraciones declaraciones;
	private SimboloCuerpo cuerpo;

	public SimboloClase(DeclaracionClase decl, SimboloDeclaraciones declaraciones) {
		this.decl = decl;
		this.declaraciones = declaraciones;
	}

	@Override
	public TipoObject getTipoSubyacente() {
		return Tipo.getTipoSafe(Tipo.Class);
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(Tipo.Class.name(), Tipo.Identificador));
		hijos.add(new SimboloTerminal(decl.getId().getId(), Tipo.Identificador));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEIZQ], Tipo.Token));

		SimboloDeclaraciones declaraciones = this.declaraciones;
		SimboloDeclaracion declaracion;
		while(declaraciones != null) {
			declaracion = declaraciones.getDeclaracion();
			hijos.add(declaracion);
			declaraciones = declaraciones.getSiguiente();
		}

		if(cuerpo != null)
			hijos.add(cuerpo);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.LLAVEDER], Tipo.Token));
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloClase";
	}

	public String getEtiquetaDeclaraciones() {
		return this.decl.getEtiquetaDeclaraciones();
	}

	public String getEtiquetaPostInicializacion() {
		return this.decl.getEtiquetaPostInicializacion();
	}

	public void setCuerpo(SimboloCuerpo cuerpo) {
		this.cuerpo = cuerpo;
	}
}
