package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;
import Errores.ErrorSemantico;

import java.util.List;
import java.util.stream.Collectors;

public class DeclaracionFuncion extends Declaracion {

    private String etiqueta;
    // Entorno vinculado a esta funcion
    private EntornoFuncion entornoDependiente;
    private boolean construyendose;

    public DeclaracionFuncion(Identificador identificador, TipoObject tipo, String etiqueta) {
        super(identificador, tipo);
        this.etiqueta = etiqueta;
        this.construyendose = true;
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

    @Override
    public int hashCode() {
        return this.etiqueta.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeclaracionFuncion)) {
            return false;
        }

        DeclaracionFuncion other = (DeclaracionFuncion)obj;
        // En este caso, suponemos que dos iguales si coinciden en nombre y parámetros.
        if (!other.identificador.getNombre().equals(identificador.getNombre())) {
            return false;
        }

        // En el caso de que las funciones estén en proceso de construcción las consideraremos como
        // si fueran diferentes. Cuando se marquen como finalizadas es cuando probablemente tengamos que comprobar si
        // hemos dejado los entornos del compilador en un estado coherente.
        // De otra forma no podemos soportar la sobrecarga de parámetros.
        if (other.construyendose || construyendose) {
            return false;
        }

        return coincidenParams(other.getTipoArgumentos());
    }

    public boolean coincidenParams(List<TipoObject> argumentoOther) {
        List<TipoObject> argumentosPropios = this.getTipoArgumentos();
        if (argumentoOther.size() != argumentosPropios.size()) {
            return false;
        }

        // TODO Esta comprobación no se lleva bien con tipos complejos ( arrays, ¿Strings?)
        TipoObject tipoDeclaracionPropia;
        TipoObject tipoDeclaracionOther;
        int numParams = argumentosPropios.size();
        boolean iguales = true;
        for(int idx = 0; idx < numParams && iguales; ++idx) {
            tipoDeclaracionPropia = argumentosPropios.get(idx);
            tipoDeclaracionOther = argumentoOther.get(idx);
            iguales = tipoDeclaracionOther.equals(tipoDeclaracionPropia);
        }

        return iguales;
    }

    public void finalizar() throws ErrorSemantico {
        this.construyendose = false;
        this.entornoDependiente.firmaCompletada();
    }

    public EntornoFuncion getEntorno() {
        return entornoDependiente;
    }

    protected List<TipoObject> getTipoArgumentos() {
        return this.entornoDependiente.getDeclaracionArgumentos().stream().map(Declaracion::getTipo).collect(Collectors.toList());
    }


}
