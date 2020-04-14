package intermedio;

public class Suma extends InstruccionTresDirecciones {
    public Suma(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.SUMA);
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

        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove.w ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove.w ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tadd.w D0, D1\n")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove D1, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");

        return sb.toString();
    }
}