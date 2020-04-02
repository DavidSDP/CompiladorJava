package intermedio;

/**
    TODO Aclarar que pasa con este tipo de operaciones en los condicionales
    Es decir, if (a<b) probablemente deba utilizar una variable temporal
    donde se guarde el valor de la comparacion se pueda usar en este punto.
*/
public class And extends InstruccionTresDirecciones {    
    public And(Operando primero, Operando segundo, Operando tercero) {
        super(InstruccionMaquina.AND);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }  
}
