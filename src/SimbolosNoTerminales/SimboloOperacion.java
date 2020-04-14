package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Checkers.TipoOperador;
import Procesador.Declaracion;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloOperacion extends Nodo implements TipoSubyacente {

    private SimboloOperacion operandoIzquierda;
    private SimboloOperacion operandoDerecha;
    private TipoOperador operador;
    private String operadorString;
    private SimboloFactor factor;

    private Boolean esOperacion = false;
    private Boolean esFactor = false;
    private Boolean esModoCompleto = false;

    private Declaracion declaracionResultado;

    public SimboloOperacion(SimboloOperacion o) {
        this.operandoIzquierda = o;
        this.esOperacion = true;
    }

    public SimboloOperacion(SimboloFactor f) {
        this.factor = f;
        this.esFactor = true;
    }

    public SimboloOperacion(SimboloOperacion operandoIzquierda, String operadorString, SimboloOperacion operacionDerecha) {
        this.operador = TipoOperador.getTipoOperador(operadorString);
        this.operandoIzquierda = operandoIzquierda;
        this.operandoDerecha = operacionDerecha;
        this.operadorString = operadorString;
        this.esModoCompleto = true;
    }

    @Override
    public List<INodo> getChildren() {
        List<INodo> hijos = new ArrayList<>();
        if (this.esOperacion) {
            hijos.add(operandoIzquierda);
            return hijos;
        }
        if (this.esFactor) {
            hijos.add(factor);
            return hijos;
        }
        hijos.add(operandoIzquierda);
        hijos.add(new SimboloTerminal(operadorString, Tipo.Token));
        hijos.add(operandoDerecha);
        return hijos;
    }

    @Override
    public String getName() {
        return "SimboloOperacion";
    }

    @Override
    public TipoObject getTipoSubyacente() {
        if (this.esOperacion)
            return this.operandoIzquierda.getTipoSubyacente();
        if (this.esFactor)
            return this.factor.getTipoSubyacente();

        // Modo Completo ->
        switch (this.operador) {
            case AritmeticoProducto:
            case AritmeticoSuma:
                return Tipo.getTipoSafe(Tipo.Integer);
            case Comparador:
            case ComparadorLogico:
            case Logico:
                return Tipo.getTipoSafe(Tipo.Boolean);
            default:
                return null;
        }
    }

    public SimboloOperacion getOperandoIzquierda() {
        return operandoIzquierda;
    }

    public void setOperandoIzquierda(SimboloOperacion operandoIzquierda) {
        this.operandoIzquierda = operandoIzquierda;
    }

    public SimboloFactor getFactor() {
        return factor;
    }

    public void setFactor(SimboloFactor factor) {
        this.factor = factor;
    }

    public TipoOperador getOperador() {
        return operador;
    }

    public void setOperador(TipoOperador operador) {
        this.operador = operador;
    }

    public void setDeclaracionResultado(Declaracion declaracionResultado) {
        this.declaracionResultado = declaracionResultado;
    }

    public Declaracion getDeclaracionResultado() {
        return declaracionResultado;
    }

}
