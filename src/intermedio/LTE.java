package intermedio;


public class LTE extends InstruccionTresDirecciones {
    public LTE(Operando primero, Operando segundo, Operando resultado) {
        super(InstruccionMaquina.LTE);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    } 

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
