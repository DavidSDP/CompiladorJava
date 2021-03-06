package Procesador;

public class Identificador {
	
	private String nombre;
	private String id;
    
    private Integer nv;
	
	public Identificador(String nombre, String id) {
		this.nombre = nombre;
		this.id = id;
		this.nv = GlobalVariables.contadorNV++;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
        @Override
        public String toString() {
            return nombre;
        }

		public Integer getNv() {
			return nv;
		}

		public void setNv(Integer nv) {
			this.nv = nv;
		}
}
