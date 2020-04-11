package intermedio;

public class Param extends InstruccionTresDirecciones {
    public Param(Operando primero) {
        super(OperacionTresDirecciones.PARAM);
        this.primero = primero;
    }  

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
