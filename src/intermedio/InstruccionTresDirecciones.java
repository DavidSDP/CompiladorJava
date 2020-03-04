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
 * 
 */
public abstract class InstruccionTresDirecciones implements MachineCodeSerializable{
    protected InstruccionMaquina operacion;
    protected Operando primero, segundo, tercero;
    
    public InstruccionTresDirecciones(InstruccionMaquina operacion) {
        this.operacion = operacion;
    }
    
    @Override
    public String toString() {
        // Probablemente esto lo podamos utilizar para hacer un debug rapido
        // con printazos. Pero probablemente tampoco lo usaría para 
        // hacer el fichero de instrucciones máquina :thinking:
        return "";
    }
}

/**
 * Primero es el origen de la copia
 * Segundo es el destino a donde se quiere copiar
 * @author jesus
 */
class Copia extends InstruccionTresDirecciones {
    public Copia(Operando primero, Operando segundo) {
        super(InstruccionMaquina.COPIA);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = null;
    }  

    @Override
    public String toMachineCode() {
        // Cada toString de un operando (alomejor debería ser un toMachineCode también)
        // debería encargarse de traducir el operador dependiendo del modo de direccionamiento! :+1:
        String primeroStr = this.primero.toString();
        String segundoStr = this.segundo.toString();
        
        return "mv" + primeroStr + segundoStr;
    }
}

/**
 * Esto se podría comprimir si la instrucción fuera OperacionAritmética
 * Pero probablemente es una mala idea
 * 
 * 
 * @author jesus
 */
class Suma extends InstruccionTresDirecciones {
    public Suma(Operando primero, Operando segundo, Operando tercero) {
        super(InstruccionMaquina.SUMA);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }  

    @Override
    public String toMachineCode() {
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
        StringBuilder sb = new StringBuilder();
        
        /**
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le sumamos el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append("mv " + primero.toString() + ", D0 \n");
        sb.append("add " + segundo.toString() + ", D0 \n");
        sb.append("mv D0, " + segundo.toString());
        
        return sb.toString();
    }
}