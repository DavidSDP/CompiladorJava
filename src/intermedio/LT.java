package intermedio;

public class LT extends InstruccionTresDirecciones {
    public LT(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.LT);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    }

    public String generateBranch() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tcmp D0, D1\n")
                .append("\tblt ").append(this.tercero.toString());

        return sb.toString();
    }

    public String generateOperation() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tcmp D0, D1\n")
                .append("\tslt D1 \n")
                .append("\tand #1, D1\n")
                .append(this.tercero.save("D1"));

//        sb.append(putActivationBlockAddressInRegister(this.primero))
//                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
//                .append(putActivationBlockAddressInRegister(this.segundo))
//                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
//                .append("\tcmp D0, D1\n")
//                .append("\tslt D1")
//                .append("\tand #1, D1")
//                .append(putActivationBlockAddressInRegister(this.tercero))
//                .append("\tmove D1, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");

        return sb.toString();
    }

    @Override
    public String toMachineCode() {
        if (this.tercero instanceof OperandoEtiqueta) {
            return this.generateBranch();
        } else {
            return this.generateOperation();
        }
    }
}
