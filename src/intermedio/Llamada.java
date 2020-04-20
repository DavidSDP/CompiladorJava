package intermedio;


import Procesador.DeclaracionFuncion;
import Procesador.Entorno;

public class Llamada extends InstruccionTresDirecciones {
    public Llamada(Operando primero, Operando segundo) {
        super(OperacionTresDirecciones.LLAMADA);
        // Callee
        this.primero = primero;
        // Caller
        this.segundo = segundo;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        DeclaracionFuncion callee = (DeclaracionFuncion)this.primero.getValor();

        if (callee.getTamanoRetorno() > 0) {
            sb.append("\tmove.w #STACK_TOP, A6\n")
                    .append("\tadd.w #").append(callee.getTamanoRetorno()).append(", \n");
        }

        if (this.segundo != null) {
            DeclaracionFuncion caller = (DeclaracionFuncion)this.segundo.getValor();
            // Determinar el access link que se tiene que almacenar en el nuevo bloque de activación
            // Ojo! Por facilidad de cálculos al acceder a las variables se almacenará el BP en el access link
            // de esa forma siempre trataremos htodo de la misma manera. Esto hace que a la hora de escalar
            // los bloques de activación tengamos que restar 1 palabra a la posición para usar el siguiente access link
            boolean mismoNivel = callee.declaradaAlMismoNivel(caller);
            if (mismoNivel) {
                // BP hace referencia al bloque de activacion antiguo y el nuevo, que esta en construccion,
                // esta referenciado por el STACK_TOP
                // El access link es el access link que tiene el caller
                sb.append("\tmove.w #BP, A5\n")
                        .append("\tsub.w #2, A5\n")
                        .append("\tadd.w #2, A6\n") // Avanzamos el puntero a la nueva posición
                        // Actualiza el access link y el stack top
                        .append("\tmove.w (A5), (A6)\n")
                        .append("\tmove.w A6, STACK_TOP\n");

            } else {
                // Ahora mismo no tenemos llamadas recursivas ni clases anidadas
                // asi que este caso no se puede dar.
                // Dadas 2 funciones A, B ambas estarán siempre declaradas al mismo nivel.
                assert false;
            }

        } else {
            // Si no hay caller estamos gestionando el main y se tiene que hacer de forma diferente:
            sb.append("\tmove.w #STACK_TOP, A6\n")
                    .append("\tadd.w #2, A6\n")
                    // Actualiza el access link y el stack top
                    .append("\tmove.w #BP, (A6)\n")
                    .append("\tmove.w A6, STACK_TOP\n");
        }
        sb.append("\tbsr update_bp\n") // Actualiza BP y Access link
                .append("\tbsr ").append(callee.getEtiqueta()).append("\n")
                // Esto funciona si los supuestos parametros han añadido el backup de la cima de la pila
                // Una alternativa es guardar el listado de declaraciones de los parametros de las funciones en la
                // declaracion de la funcion para poder calcular el offset de las variables para poder eliminarlas de la pila
                .append("\tmove.w (A7)+, STACK_TOP\n");


        return sb.toString();
    }
}
