package Procesador;

import java.util.Objects;

import Checkers.Tipo;
import Checkers.TipoObject;

public class Declaracion {

    protected Identificador identificador;
    protected TipoObject tipo;
    protected int profundidadDeclaracion;

    // Entorno donde ha sido declarada la variable.
    protected Entorno entorno;

    protected boolean isParam;

    // Este flag solo aplica a variables. En algún momento esta jerarquía debería empezar
    // a arreglarse
    protected boolean initialized;

    public void markAsInitialized() {
        this.initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Las declaraciones de funciones y clases no necesitan ni profunidad de declaracion
     * ni desplazamiento en la memoria ( no se gestionan asi )
     * @param identificador
     * @param tipo
     */
    public Declaracion(Identificador identificador, TipoObject tipo) {
        this.identificador = identificador;
        this.tipo = tipo;
        // No necesita profundidad
        this.profundidadDeclaracion = -1;
        this.isParam = false;
    }

    public Declaracion(Identificador identificador, TipoObject tipo, int profundidad) {
		this.identificador = identificador;
		this.tipo = tipo;
		this.profundidadDeclaracion = profundidad;
		this.isParam = false;
	}

    public Identificador getId() {
        return identificador;
    }

    public void setId(Identificador id) {
        this.identificador = id;
    }

    public TipoObject getTipo() {
        return tipo;
    }

    public void setTipo(TipoObject tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.identificador == null)
            return false;

        if (!(obj instanceof Declaracion)) {
            return false;
        }
        return this.identificador.equals(((Declaracion) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (profundidadDeclaracion > -1) {
            sb.append("PrD: ").append(profundidadDeclaracion).append(" ");
        }
        sb.append(tipo.toString()).append(" ").append(identificador.toString());
        return sb.toString();
    }

    public int getOcupacion() {
        return this.tipo.getSize();
    }

	public int getDesplazamiento() {
        return this.entorno.getDesplazamiento(this);
    }

    public void markParam() {
        this.isParam = true;
    }

    public int getProfundidadDeclaracion() {
	    return this.profundidadDeclaracion;
    }

    public void setEntorno(Entorno entorno) {
        this.entorno = entorno;
    }

    public boolean isParam() {
        return isParam;
    }
}



