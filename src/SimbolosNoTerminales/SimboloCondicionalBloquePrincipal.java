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
public class SimboloCondicionalBloquePrincipal extends Nodo {
    
    private SimboloClausulaCondicion clausula;
    private SimboloContenido contenido;
    
    public SimboloCondicionalBloquePrincipal(SimboloClausulaCondicion clausula, SimboloContenido contenido) {
        this.clausula = clausula;
        this.contenido = contenido;
    }
    
    public SimboloClausulaCondicion getClausula() {
        return clausula;
    }
    
    public SimboloContenido getContenido() {
        return contenido;
    }
        
}
