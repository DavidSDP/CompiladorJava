package Procesador;

import java.util.Objects;

import Checkers.Tipo;

public class Declaracion {
	
	protected Identificador identificador;
	protected Tipo tipo;
	
	public Declaracion(Identificador identificador, Tipo tipo) {
		this.identificador = identificador;
		this.tipo = tipo;
	}

	public Identificador getId() {
		return identificador;
	}

	public void setId(Identificador id) {
		this.identificador = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this.identificador == null)
			return false;
		return this.identificador.equals(((Declaracion)obj).getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(identificador);
	}
	
}



