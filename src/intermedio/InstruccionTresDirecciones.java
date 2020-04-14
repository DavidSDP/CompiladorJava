package intermedio;

interface MachineCodeSerializable {
    String toMachineCode();
}



/**
 *
 * Definicion de instruccion de codigo de tres direcciones
 * -------------------------------------------
 * | operacion | primero | segundo | tercero |
 * -------------------------------------------
 * 
 * El tercer operador se utiliza a modo de operador de destino
 * Se tendrá que traducir a código máquina en consecuencia
 *. 
 */
public abstract class InstruccionTresDirecciones implements MachineCodeSerializable {
    protected OperacionTresDirecciones operacion;
    protected Operando primero, segundo, tercero;
    
    public InstruccionTresDirecciones(OperacionTresDirecciones operacion) {
        this.operacion = operacion;
        this.primero = null;
        this.segundo = null;
        this.tercero = null;
    }
    
    @Override
    public String toString() {
        // Probablemente esto lo podamos utilizar para hacer un debug rapido
        // con printazos. Pero probablemente tampoco lo usaría para 
        // hacer el fichero de instrucciones máquina :thinking:
        return operacion + " - " + primero + " - " + segundo + " - " + tercero;
    }

    /**
     *
     * Utilidad para generar el código relacionado con la busqueda de las variables a traves de los
     * bloques de activación
     */
    public static String putActivationBlockAddressInRegister(Operando operando) {
        StringBuilder sb = new StringBuilder();
        int profundidadLlamada = operando.getProfundidad();
        int profundidadDeclaracion = operando.getValor().getProfundidadDeclaracion();
        if (profundidadLlamada > profundidadDeclaracion) {
            // Uso de una variable "global"
            sb.append("\tmove.w BP, A6\n");
            for (int distanciaEntornos = profundidadLlamada - profundidadDeclaracion; distanciaEntornos > 0; distanciaEntornos--) {
                sb.append("\tmove.w (A6), A6\n");
            }
        } else {
            // Uso de una variable local
            sb.append("\tmove.w BP, A6\n");
        }

        return sb.toString();
    }
}
