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

        sb.append(super.toMachineCode());
        sb.append(this.segundo.load("D0")) // El guardar al registro es totalmente dummy, lo que nos interesa
                .append("\tmove A6, A5\n")            // es la direccion que se deja en A6
                .append("\tadd.w #").append(declArray.getDesplazamiento()).append(", A5\n")
                .append(this.tercero.load("D1"))
                .append("\tmulu #").append(declArray.getTipoDato().getSize()).append(", D1\n")
                .append("\tadd D1, A5\n")
                // Este paso lo debemos dar de forma extra para no tener que rehacer htodo la clase Operador
                // debido a que no podemos decirle donde lo tiene que guardar
                .append(this.tercero.load("D2"))
                .append("\tmove.w D2, (A5)");

//        sb.append(putActivationBlockAddressInRegister(this.segundo))
//                .append("\tmove A6, A5\n")
//                .append("\tadd.w #").append(declArray.getDesplazamiento()).append(", A5\n")
//                .append(putActivationBlockAddressInRegister(this.tercero))
//                .append("\tmove ").append(this.tercero.getValor().getDesplazamiento()).append("(A6), D1\n")
//                .append("\tmulu #").append(declArray.getTipoDato().getSize()).append(", D1\n")
//                .append("\tadd D1, A5\n")
//                .append(putActivationBlockAddressInRegister(this.primero))
//                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), (A5)\n");

        return sb.toString();
    }
}
