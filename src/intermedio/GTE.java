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
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
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
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tcmp D0, D1\n")
                .append("\tsge D1")
                .append("\tand #1, D1")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove D1, ").append(this.tercero.getValor().getDesplazamiento()).append("(A6)\n");

// Opcion hardcore. Preferiria no morir en el intento
//                .append("\tmove.w SR, D1\n")
//                .append("and.w #9, D1")  // Limpia el resto de los bits del SR. No nos importan
//                // Sacamos el valor de N
//                .append("move D1, D0")
//                .append("lsr #3, D1")    // El bit 3 del SR es el N
//                // Sacamos el valor de V
//                .append("lsr #1, D0")    // El bit 1 del SR es el V
//                // Segun el manual del 68 V xor N == 0 es GE
//                // Por tanto comparamos si los dos números son iguales y utilizamos el Z como
//                // resultado
//                .append("cmp D0, D1")
//                .append("and.w #4, D1")  // Limpia el resto de los bits del SR. No nos importan
//                .append("lsr #2, D1")    // El tercer bit del SR es el Z
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