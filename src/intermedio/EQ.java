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

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tcmp D0, D1\n")
                .append("\tbeq ").append(this.tercero.toString()).append("\n");

        return sb.toString();
    }

    public String generateOperation() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());

        // TODO Obviamente esto no funciona para los strings. Probablemente

        // Estoy casi convencido de que la comprobacion de igualdad se puede hacer como LT y familiares
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tcmp D0, D1\n")
                .append("\tmove.w SR, D1\n")
                .append("\tand.w #4, D1\n")
                .append("\tlsr #2, D1\n")
                .append(this.tercero.save("D1"));

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
