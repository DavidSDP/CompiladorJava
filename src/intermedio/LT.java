package intermedio;

public class LT extends InstruccionTresDirecciones {
    public LT(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.LT);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    }

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}