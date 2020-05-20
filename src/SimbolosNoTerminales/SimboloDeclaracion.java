package SimbolosNoTerminales;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloDeclaracion extends Nodo implements TipoSubyacente{
	
	private String identificador;
	
	private TipoObject tipo;
	
	private Boolean esArray;
	
	private SimboloInicializacion simboloInicializacion;
	
	public SimboloDeclaracion(String id, TipoObject tipo, Boolean esArray, SimboloInicializacion simboloInicializacion) {
		this.identificador = id;
		this.tipo = tipo;
		this.esArray = esArray;
		this.simboloInicializacion = simboloInicializacion;
	}
	
	@Override
	public TipoObject getTipoSubyacente() {
		return Tipo.getTipoSafe(Tipo.Void);
	}

	public String getIdentificador() {
		return identificador;
	}

	public TipoObject getTipo() {
		return tipo;
	}

	public Boolean getEsArray() {
		return esArray;
	}

	public SimboloInicializacion getSimboloInicializacion() {
		return simboloInicializacion;
	}

}
