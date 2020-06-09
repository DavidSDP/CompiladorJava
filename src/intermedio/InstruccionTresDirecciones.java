package intermedio;

import Procesador.Declaracion;

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
    protected Operando primero;
    protected Operando segundo;
    protected Operando tercero;
	
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
    
	public void setPrimero(Operando primero) {
		this.primero = primero;
	}

	public void setSegundo(Operando segundo) {
		this.segundo = segundo;
	}

	public void setTercero(Operando tercero) {
		this.tercero = tercero;
	}



	public Declaracion getDestino() {
        if (this.tercero instanceof OperandoEtiqueta) {
            return null;
        }

        if (this.tercero != null) {
            // Instruccion con 3 operandos cuyo 3er operando debería ser el destino de la operación.
            // TODO Comprobar carga y guardado de indirecciones -.-
            return this.tercero.getValor();
        } else if (this.segundo != null) {
            // Tal como hemos formado el codigo de 3 direcciones, los operandos solo pueden estar en el 2 o en el 3 operando
            // Por tanto, si la instruccion no tiene tercer operando, pero si que tiene segundo, este tiene que ser un operando
            // destino.
            // TODO Esto no funciona para todas las instrucciones. Probablemente este es el caso generico, pero por ejemplo,
            //  para la instruccion llamada, no funciona, ya que esa i3d tiene 3 parametros, siendo el 3o el de retorno, pero
            //  puede no haberse asignado. Así que en este caso estaríamos jodidos.
            return this.segundo.getValor();
        }

        return null;
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
