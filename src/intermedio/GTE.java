package intermedio;

public class GTE extends InstruccionTresDirecciones {
    public GTE(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.GTE);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    } 

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}