package intermedio;

public class GTE extends InstruccionTresDirecciones {
    public GTE(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.GTE);
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
                .append("\tbge ").append(this.tercero.toString());

        return sb.toString();
    }

    /**
     * Por lo que se ha podido ver no existe una manera directa de poder calcular el resultado de
     * la comparacion y asignarla a una variable.
     *
     * Segun el manual del 68K para saber si un numero es mayor que otro se debe cumplir que:
     *  V xor N == 0
     *
     * Por tanto para poder hacer el cálculo se siguen los siguientes pasos:
     *   1. Comparamos los números
     *   2. Recuperamos los flags V y N
     *   3. Los desplazamos por separado hasta el LSB
     *   4. Comparamos ambos valores
     *   5. Recuperamos el flag Z
     *   6. Asignamos el valor de Z a la variable de salida
     *
     * Si los dos operandos fueran diferentes Z sería 0 y si fueran iguales Z sería 1
     */
    public String generateOperation() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.load("D1"))
                .append("\tcmp D1, D0\n")
                .append("\tsge D0")
                .append("\tand #1, D0\n")
                .append(this.tercero.save("D0"));

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

    public boolean isBranch() {
        return this.tercero instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new LT(primero, segundo, salto.getTercero());
    }

}