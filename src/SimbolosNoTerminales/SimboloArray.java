package SimbolosNoTerminales;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;
public class SimboloArray extends Nodo implements TipoSubyacente{
	
	private Integer numero;
	
	public SimboloArray() {
		this.numero = null;
	}
	
	public SimboloArray(String numero) {
		this.numero = Integer.parseInt(numero);
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		return Tipo.Array;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
}

