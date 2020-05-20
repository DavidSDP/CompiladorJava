/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimbolosNoTerminales;

import analisisSintactico.arbol.Nodo;

/**
 *
 * @author jesus
 */
public class SimboloCondicionBucle extends Nodo {
    
    private SimboloOperacion operacion;
    private String etStart;
    private String etFin;
    
    public SimboloCondicionBucle(SimboloOperacion operacion, String etStart, String etFin) {
        this.operacion = operacion;
        this.etStart = etStart;
        this.etFin = etFin;
    }
    
    public SimboloOperacion getOperacion() {
        return this.operacion;
    }

    @Override
    public String getName() {
            return "SimboloCondicional";
    }

    public String getEtiquetaStart() {
        return this.etStart;
    }

    public String getEtiquetaFin() {
        return this.etFin;
    }
}
