package intermedio;

import Procesador.DeclaracionFuncion;

public class Retorno extends InstruccionTresDirecciones {
    public Retorno(Operando primero, Operando segundo) {
        super(OperacionTresDirecciones.RETORNO);
        // Primero es el parametro que se tiene que retornar en caso de que exista un retorno.
        this.primero = primero;
        // Segundo es la declaracion de la funcion. Probablemente es totalmente inutil aqui
        this.segundo = segundo;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        // Por ahora la alternativa a mirar si la funcion tiene retorno o no es mirar si en la construccion
        // nos estan intentando pasar un retorno. Esto quiere decir que estamos confiando en que
        // el analizador sintactico cace los fallos de tipo de retorno.
        sb.append("\trts ");
        if (this.primero != null) {
            sb.append(putActivationBlockAddressInRegister(this.primero))
                    .append("\tsub.w #-4, A5\n")
                    .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), (A5)");
        }
        sb.append("\n");

        return sb.toString();
    }
}
