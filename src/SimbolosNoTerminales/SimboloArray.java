package SimbolosNoTerminales;

import analisisSintactico.arbol.Nodo;

public class SimboloArray extends Nodo {
    
    // TODO Esto no deberia ser un string !! De momento lo dejo asi porque el 
    // casteo implica tocar el analizador lexico. Y ahora estoy centrado en 
    // implementar la identificacion de arrays.
    // En el momento que se tenga que hacer comprobación de rangos, entonces si
    // que habra que hacerlo.
    private String longitud;
    

    public SimboloArray() {
        this("0"); 
    }
    
    public SimboloArray(String longitud) {
        this.longitud = longitud;
    }    
    
    public String getLongitud() {
        return this.longitud;
    }
}
