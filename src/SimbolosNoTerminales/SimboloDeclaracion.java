package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloDeclaracion extends Nodo implements TipoSubyacente{
	
	private String identificador;
	
	private Tipo tipo;
	
	private Boolean esArray;
	
	private SimboloInicializacion simboloInicializacion;
	
	public SimboloDeclaracion(String id, Tipo tipo, Boolean esArray, SimboloInicializacion simboloInicializacion) {
		this.identificador = id;
		this.tipo = tipo;
		this.esArray = esArray;
		this.simboloInicializacion = simboloInicializacion;
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Void;
	}

	public String getIdentificador() {
		return identificador;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public Boolean getEsArray() {
		return esArray;
	}

	public SimboloInicializacion getSimboloInicializacion() {
		return simboloInicializacion;
	}

}
