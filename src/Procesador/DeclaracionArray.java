package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArray;

public class DeclaracionArray extends Declaracion {

	private int size;
	private TipoObject tipoDato;

	public DeclaracionArray(Identificador identificador, TipoObject tipoDato) {
		super(identificador, tipoDato);
		this.size = -1;
	}

	public DeclaracionArray(Identificador identificador, TipoObject tipoDato, int size) {
		super(identificador, Tipo.getTipoSafe(Tipo.Array.name().toLowerCase()));
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
			// Esto debería ser tamaño del tipo * numero de elementos
			// Por ahora como valor dummy dejo 2 ( 1 no que el lint se queja joder )
			return tipoDato.getSize() * size;
		}
	}
}
