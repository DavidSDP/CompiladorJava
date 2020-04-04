package Procesador;

import Checkers.Tipo;


public class DeclaracionConstante extends Declaracion {
    
    private Object valor;
    
    public DeclaracionConstante(Identificador identificador, Tipo tipo, Object valor) {
        super(identificador, tipo);
        this.valor = valor;
    }    
}