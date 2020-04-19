package intermedio;

import Procesador.DeclaracionFuncion;

public class Preambulo extends InstruccionTresDirecciones {
    public Preambulo(Operando primero) {
        super(OperacionTresDirecciones.PREAMBULO);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        DeclaracionFuncion declaracionFuncion = (DeclaracionFuncion)this.primero.getValor();
        int memoria = declaracionFuncion.getTamanoMemoriaNecesaria();
        sb.append("\tmove.w STACK_TOP, A5\n")
                .append("\tadd.w #").append(memoria).append(", A5\n")
                .append("\tmove.w A5, STACK_TOP\n");

        return sb.toString();
    }
}
