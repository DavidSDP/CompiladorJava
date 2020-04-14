package SimbolosNoTerminales;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.Declaracion;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

public class SimboloInicializacion extends Nodo implements TipoSubyacente {

    private SimboloOperacion simboloOperacion;
    private Declaracion declaracionResultado;

    public SimboloInicializacion() {
    }

    public SimboloInicializacion(SimboloOperacion simboloOperacion) {
        this.simboloOperacion = simboloOperacion;
        this.declaracionResultado = simboloOperacion.getDeclaracionResultado();
    }

    @Override
    public TipoObject getTipoSubyacente() {
        if (simboloOperacion != null)
            return simboloOperacion.getTipoSubyacente();
        else
			return Tipo.getTipoSafe(Tipo.Void);
    }

    public SimboloOperacion getSimboloOperacion() {
        return simboloOperacion;
    }

    public Declaracion getDeclaracionResultado() {
        return declaracionResultado;
    }
}
