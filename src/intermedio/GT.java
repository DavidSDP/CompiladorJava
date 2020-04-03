package intermedio;

public class GT extends InstruccionTresDirecciones {
    public GT(Operando primero, Operando segundo, Operando resultado) {
        super(InstruccionMaquina.GT);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    } 

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
