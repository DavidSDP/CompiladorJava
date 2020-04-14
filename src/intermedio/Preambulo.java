package intermedio;

public class Preambulo extends InstruccionTresDirecciones {
    public Preambulo(Operando primero) {
        super(OperacionTresDirecciones.PREAMBULO);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        // TODO Mover el StackPointer el numero de poisiciones adecuado
        // para alojar todas las variables locales de la funcion
        return "; Missing preambulo\n";
    }
}
