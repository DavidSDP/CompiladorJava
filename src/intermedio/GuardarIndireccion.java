package intermedio;

import Procesador.DeclaracionArray;

public class GuardarIndireccion extends InstruccionTresDirecciones {
    public GuardarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.GUARDAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    /**
     *
     */
    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        DeclaracionArray declArray = (DeclaracionArray)this.segundo.getValor();
        sb.append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove A6, A5\n")
                .append("\tadd.w #").append(declArray.getDesplazamiento()).append(", A5\n")
                .append(putActivationBlockAddressInRegister(this.tercero))
                .append("\tmove ").append(this.tercero.getValor().getDesplazamiento()).append("(A6), D1\n")
                .append("\tmulu #").append(declArray.getTipoDato().getSize()).append(", D1\n")
                .append("\tadd D1, A5\n")
                .append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), (A5)\n");

        return sb.toString();
    }
}
