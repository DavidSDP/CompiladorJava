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
public abstract class InstruccionTresDirecciones implements MachineCodeSerializable, Cloneable {
    protected OperacionTresDirecciones operacion;
    private Operando primero;
	private Operando segundo;
	private Operando tercero;
	
    public InstruccionTresDirecciones(OperacionTresDirecciones operacion) {
        this.operacion = operacion;
        this.setPrimero(null);
        this.setSegundo(null);
        this.setTercero(null);
    }
    
    @Override
    public String toString() {
        // Probablemente esto lo podamos utilizar para hacer un debug rapido
        // con printazos. Pero probablemente tampoco lo usaría para 
        // hacer el fichero de instrucciones máquina :thinking:
        return operacion + " - " + getPrimero() + " - " + getSegundo() + " - " + getTercero();
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

	public void setPrimero(Operando primero) {
		this.primero = primero;
	}

	public void setSegundo(Operando segundo) {
		this.segundo = segundo;
	}

	public void setTercero(Operando tercero) {
		this.tercero = tercero;
	}
	
    @Override
    public InstruccionTresDirecciones clone(){
    	try {
			return (InstruccionTresDirecciones) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
}
