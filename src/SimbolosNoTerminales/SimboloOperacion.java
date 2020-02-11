package SimbolosNoTerminales;

import Checkers.TypeCheck;
import Procesador.Tipo;

public class SimboloOperacion implements TipoSubyacente{

	private SimboloOperacion operacion;
	private String operador;
	private SimboloFactor factor;
	
	public SimboloOperacion(SimboloOperacion o) {
		this.operacion = o;
	}
	
	public SimboloOperacion(SimboloFactor f) {
		this.factor = f;
	}
	
	public SimboloOperacion(SimboloOperacion o, String operador, SimboloFactor f) {
		this.operacion = o;
		this.operador = operador;
		this.factor = f;
	}

	@Override
	public Tipo getTipoSubyacente() {
		if(factor == null)
			return this.operacion.getTipoSubyacente();
		Tipo tipoOperador = TypeCheck.getTipoOperador(operador);
		TypeCheck.typesMatch(operacion.getTipoSubyacente(), factor.getTipoSubyacente());
		TypeCheck.typesMatch(operacion.getTipoSubyacente(), tipoOperador);
		TypeCheck.typesMatch(factor.getTipoSubyacente(), tipoOperador);
		return tipoOperador;
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

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}
	
}
