package Procesador;

import Checkers.TipoObject;

public class DeclaracionClase extends Declaracion {
    private String etiquetaDeclaraciones;
    private String etiquetaPreInicializacion;
    private String etiquetaPostInicializacion;

    private EntornoClase entornoAsociado;

    public DeclaracionClase(Identificador identificador, TipoObject tipo) {
        super(identificador, tipo);
        this.etiquetaDeclaraciones = null;
    }

    public String getEtiquetaDeclaraciones() {
        return etiquetaDeclaraciones;
    }

    public String getEtiquetaPostInicializacion() {
        return etiquetaPostInicializacion;
    }

    public String getEtiquetaPreInicializacion() {
        return etiquetaPreInicializacion;
    }

    public void setEtiquetaDeclaraciones(String etiquetaDeclaraciones) {
        this.etiquetaDeclaraciones = etiquetaDeclaraciones;
    }

    public void setEtiquetaPostInicializacion(String etiquetaPostInicializacion) {
        this.etiquetaPostInicializacion = etiquetaPostInicializacion;
    }

    public void setEtiquetaPreInicializacion(String etiquetaPreInicializacion) {
        this.etiquetaPreInicializacion = etiquetaPreInicializacion;
    }

    public void setEntornoAsociado(EntornoClase entornoAsociado) {
        this.entornoAsociado = entornoAsociado;
    }

    public EntornoClase getEntornoAsociado() {
        return entornoAsociado;
    }
}
