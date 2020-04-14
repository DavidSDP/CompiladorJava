package Procesador;

import Checkers.Tipo;
import Checkers.TipoObject;


public class DeclaracionConstante extends Declaracion {
    
    private Object valor;
    
    public DeclaracionConstante(Identificador identificador, TipoObject tipo, Object valor) {
        this(identificador, tipo, valor, 0);
    }

    public DeclaracionConstante(Identificador identificador, TipoObject tipo, Object valor, int profundidad) {
        super(identificador, tipo, profundidad);
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return tipo.toString() + " " + valor.toString();
    }


}