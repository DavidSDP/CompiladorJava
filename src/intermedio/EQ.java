package intermedio;

public class EQ extends InstruccionTresDirecciones {
    public EQ(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.EQ);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    }

    public String generateBranch() {
        StringBuilder sb = new StringBuilder();
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tcmp D0, D1\n")
                .append("\tbeq ").append(this.tercero.toString()).append("\n");

        return sb.toString();
    }

    public String generateOperation() {
        StringBuilder sb = new StringBuilder();
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tcmp D0, D1\n")
                .append("\tmove.w SR, D1\n")
                .append("and.w #4, D1")
                .append("lsr #2, D1")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove D1, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");

        return sb.toString();
    }

    @Override
    public String toMachineCode() {
        /**
         * Disclaimer: El codigo que estás a punto de leer es una puta mierda
         * y htodo porque no tenemos diferenciadas las instrucciones de 3 direcciones
         * para las condiciones en los saltos de las operaciones que se asignan a algún lado.
         */
        if (this.tercero instanceof OperandoEtiqueta) {
            return generateBranch();
        } else {
            return generateOperation();
        }
    }

}
