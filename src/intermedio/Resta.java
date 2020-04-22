package intermedio;

public class Resta extends InstruccionTresDirecciones {
    public Resta(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.RESTA);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tsub.w D1, D0\n")
                .append(this.tercero.save("D0"));

        return sb.toString();
    }
}