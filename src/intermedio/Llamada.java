package intermedio;


public class Llamada extends InstruccionTresDirecciones {
    public Llamada(Operando primero) {
        super(OperacionTresDirecciones.LLAMADA);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
