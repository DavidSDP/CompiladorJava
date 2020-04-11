package Procesador;

import Checkers.Tipo;


public class DeclaracionConstante extends Declaracion {
    
    private Object valor;
    
    public DeclaracionConstante(Identificador identificador, Tipo tipo, Object valor) {
        this(identificador, tipo, valor, 0);
    }

    public DeclaracionConstante(Identificador identificador, Tipo tipo, Object valor, int desplazamiento) {
        super(identificador, tipo, desplazamiento);
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return tipo.toString() + " " + valor.toString();
    }


}