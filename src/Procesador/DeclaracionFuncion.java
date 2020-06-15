package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;

public class DeclaracionFuncion extends Declaracion {

    private String etiqueta;
    // Entorno vinculado a esta funcion
    private EntornoFuncion entornoDependiente;

    public DeclaracionFuncion(Identificador identificador, TipoObject tipo, String etiqueta) {
        super(identificador, tipo);
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return tipo.toString() + " " + identificador.toString() + "(" + this.etiqueta + ")";
    }

    public void setEntornoDependiente(EntornoFuncion entorno) {
        this.entornoDependiente = entorno;
    }

    public int getTamanoMemoriaNecesaria() {
        return entornoDependiente.getTamanoTotalVariables();
    }

    public boolean declaradaAlMismoNivel(DeclaracionFuncion otra) {
        assert entornoDependiente != null && otra.entornoDependiente != null;
        return entornoDependiente.getProfundidad() == otra.entornoDependiente.getProfundidad();
    }

    public int getTamanoRetorno() {
        if (tipo != null)
            return tipo.getSize();
        else
            return 0;
    }

    public boolean hasRetorno() {
        // TODO Check if void messes with this
        return tipo != null;
    }

    public boolean isReturnIsComplexType() {
        // TODO Check if an array return is stored as array :thinking:
        return Tipo.String.equals(tipo.getTipo()) || Tipo.Array.equals(tipo.getTipo());
    }

    public boolean hasParams() {
        return entornoDependiente.getListaArgumentos().size() > 0;
    }
}
