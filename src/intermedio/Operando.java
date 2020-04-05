package intermedio;

import Procesador.Declaracion;

public class Operando {
    protected ModoDireccionamiento modo;

    // Ahora mismo Declaracion puede contener una variable o una constante.
    // Asi que no es necesario diferenciar el tipo de valor que estamos manejando
    // en este momento
    protected  Declaracion valor;     
    
    public Operando(Declaracion valor) {
        System.out.println(valor);
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        
        return valor.toString(); 
    }
}
