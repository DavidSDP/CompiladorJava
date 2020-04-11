package Procesador;

import Checkers.Tipo;
import SimbolosNoTerminales.SimboloArray;

public class DeclaracionArray extends Declaracion {
	
	private SimboloArray simboloArray;
	private Tipo tipoDato;
	
	public DeclaracionArray(Identificador identificador, Tipo tipoDato, SimboloArray simboloArray) {
		// TODO Falta pasarle el desplazamiento a esta cosa
		super(identificador, Tipo.Array);
		this.setSimboloArray(simboloArray);
		this.setTipoDato(tipoDato);
	}

	public SimboloArray getSimboloArray() {
		return simboloArray;
	}

	public void setSimboloArray(SimboloArray simboloArray) {
		this.simboloArray = simboloArray;
	}

	public Tipo getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(Tipo tipoDato) {
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
			return 2 * this.simboloArray.getNumero();
		}
	}
}
