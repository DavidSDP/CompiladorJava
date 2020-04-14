package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;

public class DeclaracionFuncion extends Declaracion {

    private String etiqueta;

    public DeclaracionFuncion(Identificador identificador, TipoObject tipo, String etiqueta) {
        super(identificador, tipo);
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }

    @Override
    public String toString() {
        return tipo.toString() + " " + identificador.toString() + "(" + this.etiqueta + ")";
    }
}
