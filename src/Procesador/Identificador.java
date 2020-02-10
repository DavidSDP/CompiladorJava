package Procesador;

public class Identificador {
	
	private String id;
	private Tipo tipo;
	
	public Identificador(String id, Tipo tipo) {
		this.id = id;
		this.tipo = tipo;
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
		return this.id.equals(((Identificador)obj).getId());
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
