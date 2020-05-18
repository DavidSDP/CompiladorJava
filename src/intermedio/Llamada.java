package intermedio;


import CodigoMaquina.DataRegister;
import Procesador.DeclaracionFuncion;
import Procesador.Entorno;

public class Llamada extends InstruccionTresDirecciones {
    public Llamada(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.LLAMADA);
        // Callee
        this.primero = primero;
        // Caller
        this.segundo = segundo;
        // Return container
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        DeclaracionFuncion callee = (DeclaracionFuncion)this.primero.getValor();

        sb.append(super.toMachineCode());
        sb.append("\tmove.l STACK_TOP, A6\n");
        if (!callee.hasParams()) {
            // Si la funcion no tiene parametros no se ha movido el STACK_TOP desde la ultima insercion de
            // datos y sigue en el dato anterior, así que aqui lo tenemos que mover
            sb.append("\tmove.l STACK_TOP, -(A7)\n")
                    .append("\tadd.l #2, A6\n");

        }

        if (callee.getTamanoRetorno() > 0) {
            sb.append("\tadd.l #").append(callee.getTamanoRetorno()).append(", A6\n");
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
                sb.append("\tmove.l BP, A5\n")
                        .append("\tsub.l #4, A5\n")
                        // Actualiza el access link y el stack top
                        .append("\tmove.l (A5), (A6)\n")
                        // Forzamos a que el stack top apunte a la última palabra de la pila ( la pila se gestiona en palabras )
                        .append("\taddq.l #2, A6\n")
                        .append("\tmove.l A6, STACK_TOP\n");

            } else {
                // Ahora mismo no tenemos llamadas recursivas ni clases anidadas
                // asi que este caso no se puede dar.
                // Dadas 2 funciones A, B ambas estarán siempre declaradas al mismo nivel.
                assert false;
            }

        } else {
            // Si no hay caller estamos gestionando el main y se tiene que hacer de forma diferente:
            // Saltamos el BP actual
            sb.append("\tadd.l #2, A6\n")
                    // y actualiza el access link y el stack top
                    .append("\tmove.l BP, (A6)\n")
                    .append("\taddq.l #2, A6\n")
                    .append("\tmove.l A6, STACK_TOP\n");
        }

        // Actualizamos el estado del programa y llamamos a la función
        sb.append("\tbsr update_bp\n") // Actualiza BP y Access link
                .append("\tbsr ").append(callee.getEtiqueta()).append("\n");

        // Puede que la función tenga retorno y que el que llama no lo esté gestionando
        if (this.tercero != null) {
            sb.append("\tmove.l BP, A6\n")
                    .append("\tsub.l #").append(2 + callee.getTamanoRetorno()).append(", A6\n")
                    // TODO Ahora mismo el tamaño asumido para el retorno es un word. Esto debe cambiar
                    //  para tener en cuenta el tipo retornado.
                    .append("\tmove.w (A6), D5\n")
                    .append("\tbsr restore_bp\n")
                    // TODO Esto asume que el retorno es de 1 palabra (Bad idea)
                    .append(this.tercero.save(DataRegister.D5));
        } else {
            sb.append("\tbsr restore_bp\n");
        }

        // Esto funciona si los supuestos parametros han añadido el backup de la cima de la pila
        // Una alternativa es guardar el listado de declaraciones de los parametros de las funciones en la
        // declaracion de la funcion para poder calcular el offset de las variables para poder eliminarlas de la pila
        sb.append("\tmove.l (A7)+, STACK_TOP\n");


        return sb.toString();
    }
}
