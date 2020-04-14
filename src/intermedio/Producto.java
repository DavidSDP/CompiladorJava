package intermedio;

public class Producto extends InstruccionTresDirecciones {
    public Producto(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.PRODUCTO);
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

        /*
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le div el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove.w ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove.w ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tmuls D0, D1\n")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove.w D1, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");

        return sb.toString();
    }
}