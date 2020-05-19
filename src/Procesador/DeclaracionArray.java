package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArray;

public class DeclaracionArray extends Declaracion {

	private int size;
	private TipoObject tipoDato;

	public DeclaracionArray(Identificador identificador, TipoObject tipoDato, int profundidad) {
		this(identificador, tipoDato, -1, profundidad);
	}

	public DeclaracionArray(Identificador identificador, TipoObject tipoDato, int size, int profundidad) {
		super(identificador, Tipo.getTipoSafe(Tipo.Array.name().toLowerCase()), profundidad);
		this.size = size;
		this.setTipoDato(tipoDato);
	}

	public TipoObject getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(TipoObject tipoDato) {
		this.tipoDato = tipoDato;
	}

	public int getLongitudArray() {
		return this.size;
	}

	@Override
	public int getOcupacion() {
		if (this.size == -1) {
			System.out.println("Se ha intentado acceder a la ocupacion de un array sin tamaño");
			return -1;
		} else {
			// El tamaño ocupado en bloques de activación es la de un descriptor, por tanto,
			// NO es el tamaño calculado de numero de elemntos * tamaño elemento
			return tipo.getSize();
		}
	}
}
