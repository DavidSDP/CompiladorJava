package SimbolosNoTerminales;

import analisisSintactico.arbol.Nodo;


public class SimboloClausulaCondicion extends Nodo {
    
    private SimboloOperacion operacion;
    
    public SimboloClausulaCondicion(SimboloOperacion operacion) {
        this.operacion = operacion;
    }
    
    public SimboloOperacion getOperacion() {
        return this.operacion;
    }
}
