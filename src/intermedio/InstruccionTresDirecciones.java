package intermedio;

import Procesador.Declaracion;
import Procesador.DeclaracionConstante;

import java.util.ArrayList;

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
 * Se tendrá que traducir a código máquina en consecuencia.
 * 
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
        StringBuilder sb = new StringBuilder();
        sb.append(operacion)
                .append(" - ")
                .append(primero);
        if (segundo != null) {
            sb.append(" - ").append(segundo);
        }

        if (tercero != null) {
            sb.append(" - ").append(tercero);
        }
        return sb.toString();
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

    public boolean primeroEsConstante() {
        return !(this.primero instanceof OperandoEtiqueta) && this.primero.getValor() instanceof DeclaracionConstante;
    }

    public boolean segundoEsConstante() {
        return !(this.segundo instanceof OperandoEtiqueta) && this.segundo.getValor() instanceof DeclaracionConstante;
    }

    public abstract boolean esDefinicion();

    /**
     * Obtiene el destino de esta instrucción.
     * Se ha puesto aquí exclusivamente para poder manejar todos los casos generales de la
     * misma forma.
     *
     * Probablemente no cumple para todos los casos. Por tanto, se debe adecuar en las
     * subclases que procedan.
     */
    public Declaracion getDestino() {
        if (this.tercero instanceof OperandoEtiqueta) {
            return null;
        }

        if (this.tercero != null) {
            // Instruccion con 3 operandos cuyo 3er operando debería ser el destino de la operación.
            return this.tercero.getValor();
        } else if (this.segundo != null) {
            // Tal como hemos formado el codigo de 3 direcciones, los operandos solo pueden estar en el 2 o en el 3 operando
            // Por tanto, si la instruccion no tiene tercer operando, pero si que tiene segundo, este tiene que ser un operando
            // destino.
            return this.segundo.getValor();
        }

        return null;
    }

    /**
     * Por norma general el primer y segundo argumentos se consideran no destino.
     * Obviamente, esto depende de la instrucción, así que se tiene que sobreescribir en esos
     * casos donde esta condición no es cierta.
     */
    public ArrayList<Declaracion> getArgumentos() {
        ArrayList<Declaracion> argumentos = new ArrayList<>();
        argumentos.add(primero.getValor());
        argumentos.add(segundo.getValor());
        return argumentos;
    }

    @Override
    public int hashCode() {
        String str = operacion.toString();
        if (this.primero != null) {
            str += " " + this.primero.hashCode();
        }

        if (this.segundo != null) {
            str += " " + this.segundo.hashCode();
        }

        if (this.tercero != null) {
            str += " " + this.tercero.hashCode();
        }

//        System.out.print(this);
//        System.out.print(" ");
//        System.out.print(str);
//        System.out.print(" ");
//        System.out.println(str.hashCode());

        return str.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InstruccionTresDirecciones)) {
            return false;
        }

        InstruccionTresDirecciones other = (InstruccionTresDirecciones)obj;

        if (other.operacion != operacion) {
            return false;
        }

        // Primer intento ... Vamos a empezar comprobando solo si los punteros de los operandos son los mismos.
        return other.primero == primero && other.segundo == segundo && other.tercero == tercero;
    }
}
