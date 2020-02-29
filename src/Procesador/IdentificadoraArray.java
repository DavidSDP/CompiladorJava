/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesador;

import Checkers.Tipo;

/**
 *
 * @author jesus
 */
public class IdentificadoraArray extends Identificador {
    
    private Tipo tipoElemento;
    // minRango es puro tramite. En nuestro caso los arrays empiezan a indexarse
    // en 0.
    private int minRango;
    private int maxRango;
    
    public IdentificadoraArray(String id, Tipo tipo) {
        super(id, Tipo.Array);
        this.tipoElemento = tipo;
        this.minRango = this.maxRango = 0;
    }
    
    public IdentificadoraArray(String id, Tipo tipo, int size) {
        super(id, Tipo.Array);
        this.tipoElemento = tipo;
        this.minRango = 0;
        
        // Esto no debería llegar a pasar. Probablemente habría que comprobar
        // en tiempo de compilación que los tamanos de los arrays superan los 0 
        // elementos y lanzar algún tipo de error semántico
        this.maxRango = Math.max(0, size - 1);
    }
    
    public Tipo getTipoElemento() {
        return this.tipoElemento;
    }
    
}
