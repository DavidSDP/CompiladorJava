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

    @Override
    public String toMachineCode() {
        return ";" + toString() + "\n";
    }

    public OperacionTresDirecciones getOperacion() {
        return operacion;
    }

    public Operando getPrimero() {
        return primero;
    }

    public Operando getSegundo() {
        return segundo;
    }

    public Operando getTercero() {
        return tercero;
    }
}
