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
    
    public SimboloCondicionBucle(SimboloOperacion operacion) {
        this.operacion = operacion;
    }
    
    public SimboloOperacion getOperacion() {
        return this.operacion;
    }

    @Override
    public String getName() {
            return "SimboloCondicional";
    }
}
