package intermedio;

import Procesador.DeclaracionFuncion;

public class Retorno extends InstruccionTresDirecciones {
    public Retorno(Operando primero, Operando segundo) {
        super(OperacionTresDirecciones.RETORNO);
        // Primero es la declaracion de la funcion
        this.primero = primero;
        // Segundo es el parametro que se tiene que retornar en caso de que exista un retorno.
        this.segundo = segundo;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();

        if (this.segundo != null) {
            sb.append(putActivationBlockAddressInRegister(this.segundo))
                    .append("\tmove.w BP, A5\n")
                    .append("\tsub.w #-4, A5\n") // Esto tiene pinta de depender del tipo de retorno
                    .append("\tmove ").append(this.segundo.getValor().getDesplazamiento()).append("(A6), (A5)\n");
        }

        DeclaracionFuncion decl = (DeclaracionFuncion) this.primero.getValor();
        int memoriaReservada = decl.getTamanoMemoriaNecesaria();
        if (memoriaReservada > 0) {
            // Si hemos reservado memoria para variables locales, se tiene que liberar.
            sb.append("\tmove.w #STACK_TOP, A6\n")
                    .append("\tsub.w #").append(memoriaReservada).append(", A6\n")
                    .append("\tmove.w A6, STACK_TOP\n");
        }
        sb.append("\trts\n");

        return sb.toString();
    }
}
