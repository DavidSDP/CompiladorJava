package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import Procesador.Declaracion;

public abstract class Param extends InstruccionTresDirecciones {

    private boolean isLast;

    public Param(OperacionTresDirecciones operacion, Operando primero) {
        super(operacion);
        this.setPrimero(primero);
    }

    public void markLastParam() {
        this.isLast = true;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toMachineCode());

        if (this.isLast) {
            // El ultimo parametro de la llamada es el que se pone primero en la pila por tanto
            // es el que tiene que actualizar el bp para evitar problemas de solapamiento de memoria
            // Además almacenamos el stack top en la pila para poder recuperarla más adelante
            sb.append("\tmove.l STACK_TOP, -(A7)\n")
                    .append("\tmove.l STACK_TOP, A5\n")
                    .append("\tadd.l #2, A5\n")
                    .append("\tmove.l A5, STACK_TOP\n");

        }

        return sb.toString();
    }
}
