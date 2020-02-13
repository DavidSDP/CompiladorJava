package Procesador;

import java.util.Objects;

import Checkers.Tipo;

public class Identificador {
	
	private String id;
	private Tipo tipo;
	private Boolean esConstante;
	
	public Identificador(String id, Tipo tipo) {
		this.id = id;
		this.tipo = tipo;
		this.esConstante = false;
	}
	
	public Identificador(String id, Tipo tipo, Boolean esConstante) {
		this.id = id;
		this.tipo = tipo;
		this.esConstante = esConstante;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		if(this.id == null)
			return false;
		return this.id.equals(((Identificador)obj).getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Boolean getEsConstante() {
		return esConstante;
	}
	
}
