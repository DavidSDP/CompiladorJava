package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArray;

public class DeclaracionArray extends Declaracion {
	
	private SimboloArray simboloArray;
	private TipoObject tipoDato;
	
	public DeclaracionArray(Identificador identificador, TipoObject tipoDato, SimboloArray simboloArray) {
		super(identificador, Tipo.getTipoSafe(Tipo.Array.name().toLowerCase()));
		this.setSimboloArray(simboloArray);
		this.setTipoDato(tipoDato);
	}

	public SimboloArray getSimboloArray() {
		return simboloArray;
	}

	public void setSimboloArray(SimboloArray simboloArray) {
		this.simboloArray = simboloArray;
	}

	public TipoObject getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(TipoObject tipoDato) {
		this.tipoDato = tipoDato;
	}

	public int getLongitudArray() {
		return this.simboloArray.getNumero();
	}

	@Override
	public int getOcupacion() {
		if (this.simboloArray.getNumero() == null) {
			System.out.println("Se ha intentado acceder a la ocupacion de un array sin tamaño");
			return -1;
		} else {
			// Esto debería ser tamaño del tipo * numero de elementos
			// Por ahora como valor dummy dejo 2 ( 1 no que el lint se queja joder )
			return tipoDato.getSize() * this.simboloArray.getNumero();
		}
	}
}
