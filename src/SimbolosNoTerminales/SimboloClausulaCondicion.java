package SimbolosNoTerminales;

import analisisSintactico.arbol.Nodo;


public class SimboloClausulaCondicion extends Nodo {
    
    private SimboloOperacion operacion;
    private String etiqueta;
    
    public SimboloClausulaCondicion(SimboloOperacion operacion, String etiqueta) {
        this.operacion = operacion;
        this.etiqueta = etiqueta;
    }
    
    public SimboloOperacion getOperacion() {
        return this.operacion;
    }
    
    public String getEtiqueta() {
        return this.etiqueta;
    }
}
