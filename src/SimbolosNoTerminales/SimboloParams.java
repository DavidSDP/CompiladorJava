package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;
import intermedio.InstruccionTresDirecciones;
import intermedio.Param;

public class SimboloParams extends Nodo implements TipoSubyacente{
	
	private SimboloOperacion operacion;
	private SimboloParams nextParam;
	private Boolean esPrimeraproduccion;

	private Param param;
	
	public SimboloParams(SimboloOperacion o, SimboloParams n, Boolean esPrimeraproduccion) {
		this.setOperacion(o);
		this.setNextParam(n);
		this.esPrimeraproduccion = esPrimeraproduccion;
	}

	public SimboloParams(SimboloOperacion o, SimboloParams n, Boolean esPrimeraproduccion, Param param) {
		this.setOperacion(o);
		this.setNextParam(n);
		this.esPrimeraproduccion = esPrimeraproduccion;
		this.param = param;
	}

	public SimboloOperacion getOperacion() {
		return operacion;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		if(!esPrimeraproduccion)
			hijos.add(new SimboloTerminal(sym.terminalNames[sym.COMA], Tipo.Token));
		hijos.add(operacion);
		if(nextParam != null)
			hijos.add(nextParam);
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloParams";
	}

	@Override
	public TipoObject getTipoSubyacente() {
		return this.operacion.getTipoSubyacente();
	}

	public void setOperacion(SimboloOperacion operacion) {
		this.operacion = operacion;
	}

	public SimboloParams getNextParam() {
		return nextParam;
	}

	public void setNextParam(SimboloParams nextParam) {
		this.nextParam = nextParam;
	}

	public void markLastParam() {
		this.param.markLastParam();
	}
}
