package Procesador;

import java.util.Objects;

import Checkers.Tipo;

public class Declaracion {
	
	private Identificador identificador;
	private Tipo tipo;
	private Boolean esConstante;
	
	public Declaracion(Identificador identificador, Tipo tipo) {
		this.identificador = identificador;
		this.tipo = tipo;
		this.esConstante = false;
	}
	
	public Declaracion(Identificador identificador, Tipo tipo, Boolean esConstante) {
		this.identificador = identificador;
		this.tipo = tipo;
		this.esConstante = esConstante;
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

	public Boolean getEsConstante() {
		return esConstante;
	}
	
}
