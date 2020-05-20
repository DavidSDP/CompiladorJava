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
    private String etiqueta;
    
    public SimboloCondicionalBloquePrincipal(SimboloClausulaCondicion clausula, SimboloContenido contenido, String etiqueta) {
        this.clausula = clausula;
        this.contenido = contenido;
        this.etiqueta = etiqueta;
    }
    
    public SimboloClausulaCondicion getClausula() {
        return this.clausula;
    }
    
    public SimboloContenido getContenido() {
        return this.contenido;
    }
    
    public String getEtiqueta() {
        return this.etiqueta;
    }
        
}
