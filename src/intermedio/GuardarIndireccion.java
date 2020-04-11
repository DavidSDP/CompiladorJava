package intermedio;

public class GuardarIndireccion extends InstruccionTresDirecciones {
    public GuardarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.GUARDAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        return null;
    }
}
