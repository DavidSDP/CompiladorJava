package intermedio;

public class Division extends InstruccionTresDirecciones {
    public Division(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.DIVISION);
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
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tdivs D1, D0\n")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove D0, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");
        
        return sb.toString();
    }
}