package Procesador;

import Checkers.TipoObject;

public class DeclaracionClase extends Declaracion {
    private String etiquetaDeclaraciones;
    private String etiquetaPostInicializacion;

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

    public void setEtiquetaDeclaraciones(String etiquetaDeclaraciones) {
        this.etiquetaDeclaraciones = etiquetaDeclaraciones;
    }

    public void setEtiquetaPostInicializacion(String etiquetaPostInicializacion) {
        this.etiquetaPostInicializacion = etiquetaPostInicializacion;
    }
}
