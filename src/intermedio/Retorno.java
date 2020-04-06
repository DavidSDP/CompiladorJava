package intermedio;

public class Retorno extends InstruccionTresDirecciones {
    public Retorno(Operando primero) {
        super(OperacionTresDirecciones.RETORNO);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
