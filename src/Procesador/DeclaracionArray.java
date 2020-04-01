package Procesador;

import Checkers.Tipo;
import SimbolosNoTerminales.SimboloArray;

public class DeclaracionArray extends Declaracion{
	
	private SimboloArray simboloArray;
	private Tipo tipoDato;
	
	public DeclaracionArray(Identificador identificador, Tipo tipoDato, SimboloArray simboloArray) {
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

}
