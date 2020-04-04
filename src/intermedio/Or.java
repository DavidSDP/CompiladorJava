package intermedio;

/**
    Mirar comentario en la clase AND
*/
public class Or extends InstruccionTresDirecciones {
    public Or(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.OR);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }  

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
