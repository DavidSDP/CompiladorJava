package intermedio;

public class GT extends InstruccionTresDirecciones {
    public GT(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.GT);
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
                .append("\tbgt ").append(this.tercero.toString());

        return sb.toString();
    }

    public String generateOperation() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                // Interpretación del cmp par sgt: D0 es mayor que D1 ?
                .append("\tcmp D1, D0\n")
                .append("\tsgt D0 \n")
                .append("\tand #1, D0\n")
                .append(this.tercero.save("D0"));

        return sb.toString();
    }

    @Override
    public String toMachineCode() {
        if (isBranch()) {
            return this.generateBranch();
        } else {
            return this.generateOperation();
        }
    }

    public boolean isBranch() {
        return this.tercero instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new LTE(primero, segundo, salto.getTercero());
    }

}
