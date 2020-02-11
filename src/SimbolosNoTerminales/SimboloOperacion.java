package SimbolosNoTerminales;

import java.util.List;

import Checkers.Tipo;
import Checkers.TipoOperador;
import Procesador.TipoSubyacente;

public class SimboloOperacion implements TipoSubyacente, Nodo{

	private SimboloOperacion operacion;
	private TipoOperador operador;
	private SimboloFactor factor;
	
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
	
	public SimboloOperacion(SimboloOperacion o, TipoOperador operador, SimboloFactor f) {
		this.operacion = o;
		this.operador = operador;
		this.factor = f;
		this.esModoCompleto = true;
	}
	
//		Tipo tipoOperador = TypeCheck.getTipoOperador(operador);
//		TypeCheck.typesMatch(operacion.getTipoSubyacente(), factor.getTipoSubyacente());
//		TypeCheck.typesMatch(operacion.getTipoSubyacente(), tipoOperador);
//		TypeCheck.typesMatch(factor.getTipoSubyacente(), tipoOperador);
//		return tipoOperador;

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
	
}
