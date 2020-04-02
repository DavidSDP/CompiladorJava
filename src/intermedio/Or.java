package intermedio;

/**
    Mirar comentario en la clase AND
*/
public class Or extends InstruccionTresDirecciones {
    public Or(Operando primero, Operando segundo, Operando tercero) {
        super(InstruccionMaquina.OR);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }  
}
