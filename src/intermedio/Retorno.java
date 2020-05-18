package intermedio;

import CodigoMaquina.DataRegister;
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

        sb.append(super.toMachineCode());
        if (this.segundo != null) {
            // El offset se calcula desplazando desde BP el access link + el tamano del
            // tipo que retornamos
            int returnOffset = this.segundo.getValor().getOcupacion() + 2;

            sb.append("\tmove.w BP, A5\n")
                    // Probablemente esto se podría hacer con una indirección sobre A5
                    .append("\tsub.w #").append(returnOffset).append(", A5\n")
                    .append(this.segundo.load(DataRegister.D0))
                    .append("\tmove.w D0, (A5)\n");
        }

        DeclaracionFuncion decl = (DeclaracionFuncion) this.primero.getValor();
        int memoriaReservada = decl.getTamanoMemoriaNecesaria();
        if (memoriaReservada > 0) {
            // Si hemos reservado memoria para variables locales, se tiene que liberar.
            sb.append("\tmove.w STACK_TOP, A6\n")
                    .append("\tsub.w #").append(memoriaReservada).append(", A6\n")
                    .append("\tmove.w A6, STACK_TOP\n");
        }
        sb.append("\trts\n");

        return sb.toString();
    }
}
