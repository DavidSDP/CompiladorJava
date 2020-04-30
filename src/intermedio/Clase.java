package intermedio;

import Procesador.DeclaracionClase;
import Procesador.EntornoClase;

public class Clase extends InstruccionTresDirecciones {
    public Clase(Operando primero) {
        super(OperacionTresDirecciones.CLASE);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        EntornoClase entorno = ((DeclaracionClase)this.primero.getValor()).getEntornoAsociado();

        sb.append(super.toMachineCode());
        sb.append("\tmove.w STACK_TOP, A6\n")
                .append("\tadd.w #").append(entorno.getTamanoTotalVariables()).append(", A6\n")
                .append("\tmove.w A6, STACK_TOP\n");

        return sb.toString();
    }
}
