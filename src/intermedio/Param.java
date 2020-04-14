package intermedio;

import Procesador.Declaracion;

public class Param extends InstruccionTresDirecciones {
    public Param(Operando primero) {
        super(OperacionTresDirecciones.PARAM);
        this.primero = primero;
    }  

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        Declaracion valor = this.primero.getValor();
        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tPUSH_PARAM ").append(valor.getDesplazamiento()).append("(A6), #").append(valor.getOcupacion()).append("\n");

        return sb.toString();
    }
}
