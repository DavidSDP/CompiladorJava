package intermedio;

public class CargarIndireccion extends InstruccionTresDirecciones {
    public CargarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.CARGAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        return null;
    }
}
