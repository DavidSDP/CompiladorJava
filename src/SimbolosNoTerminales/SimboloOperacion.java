package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoOperador;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloOperacion extends Nodo implements TipoSubyacente{

	private SimboloOperacion operacion;
	private TipoOperador operador;
	private String operadorString;
	private SimboloFactor factor;
	private SimboloOperacion operacionDerecha;
	
	private Boolean esOperacion = false;
	private Boolean esFactor = false;
	private Boolean esModoCompleto = false;
	
	public SimboloOperacion(SimboloOperacion o) {
		this.operacion = o;
		this.esOperacion = true;
	}
	
	public SimboloOperacion(SimboloFactor f) {
		this.factor = f;
		this.esFactor = true;
	}
	
	public SimboloOperacion(SimboloOperacion o, String operadorString, SimboloOperacion operacionDerecha) {
		this.operacion = o;
		this.operador = TipoOperador.getTipoOperador(operadorString);
		this.operadorString = operadorString;
		this.operacionDerecha = operacionDerecha;
		this.esModoCompleto = true;
	}

	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		if(this.esOperacion) {
			hijos.add(operacion);
			return hijos;
		}
		if(this.esFactor) {
			hijos.add(factor);
			return hijos;
		}
		hijos.add(operacion);
		hijos.add(new SimboloTerminal(operadorString, Tipo.Token));
		hijos.add(operacionDerecha);
		return hijos;
	}

	@Override
	public String getName() {
		return "SimboloOperacion";
	}

	@Override
	public Tipo getTipoSubyacente() {
		if(this.esOperacion)
			return this.operacion.getTipoSubyacente();
		if(this.esFactor)
			return this.factor.getTipoSubyacente();
		
		// Modo Completo ->
		switch(this.operador) {
			case AritmeticoProducto:
				return Tipo.Integer;
			case AritmeticoSuma:
				return Tipo.Integer;
			case Comparador:
				return Tipo.Boolean;
			case ComparadorLogico:
				return Tipo.Boolean;
			case Logico:
				return Tipo.Boolean;
			default:
				return null;
				
		}
	}

	public SimboloOperacion getOperacion() {
		return operacion;
	}

	public void setOperacion(SimboloOperacion operacion) {
		this.operacion = operacion;
	}

	public SimboloFactor getFactor() {
		return factor;
	}

	public void setFactor(SimboloFactor factor) {
		this.factor = factor;
	}

	public TipoOperador getOperador() {
		return operador;
	}

	public void setOperador(TipoOperador operador) {
		this.operador = operador;
	}
	
}
