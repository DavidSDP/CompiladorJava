package intermedio;

import Checkers.Tipo;
import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.Size;
import CodigoMaquina.StackPointer;
import CodigoMaquina.Variables;
import CodigoMaquina.especiales.Contenido;
import CodigoMaquina.especiales.Indireccion;
import CodigoMaquina.especiales.Literal;
import CodigoMaquina.especiales.PostIncremento;
import CodigoMaquina.especiales.PreDecremento;
import CodigoMaquina.especiales.Restore;
import Procesador.Declaracion;
import Procesador.DeclaracionArray;
import Procesador.DeclaracionConstante;


public class Operando {
    // Ahora mismo Declaracion puede contener una variable o una constante.
    // Asi que no es necesario diferenciar el tipo de valor que estamos manejando
    // en este momento
    protected Declaracion valor;

    // El operando refleja que variable/constante se está utilizando en el cálculo
    // y a que profundidad se está usando.
    // Esta profundidad sirve para poder calcular cuantos bloques de activación
    // se tienen que escalar para poder llegar al entorno local de la variable.
    protected int profundidad;

    public Operando(Declaracion valor, int profundidad) {
        this.valor = valor;
        this.profundidad = profundidad;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Forma un string del estilo [PrC: 10, XXXXXXX]
        sb.append("[PrC: ").append(this.profundidad).append(", ").append(valor.toString()).append("]");
        return sb.toString();
    }

    /**
     * Utilidad para generar el código relacionado con la busqueda de las variables a traves de los
     * bloques de activación
     * <p>
     * Esto NO esta bien hecho. Ahora mismo escalamos por los diferentes bloques de activación
     * que son los inmediatamente superiores en el orden de llamada. Pero esto no refleja
     * los ambitos de ejecución.
     * <p>
     * Para arreglarlo, realmente se tiene que escalar por los access links que realmente contienen
     * el puntero al entorno contenedor ( no tiene porque ser el bloque de activacion anterior )
     */
    public BloqueInstrucciones putActivationBlockAddressInRegister() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        int profundidadLlamada = this.getProfundidad();
        int profundidadDeclaracion = this.getValor().getProfundidadDeclaracion();
        if (profundidadLlamada > profundidadDeclaracion) {
            // Uso de una variable "global"
	        bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, AddressRegister.A6));
            for (int distanciaEntornos = profundidadLlamada - profundidadDeclaracion; distanciaEntornos > 0; distanciaEntornos--) {
    	        bI.add(new Instruccion(OpCode.SUBQ, Size.L, Literal.__(4), AddressRegister.A6));
    	        bI.add(new Instruccion(OpCode.MOVE, Size.L, Contenido.__(AddressRegister.A6), AddressRegister.A6));
            }
        } else {
            // Uso de una variable local
	        bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, AddressRegister.A6));
        }
        return bI;
    }

    public BloqueInstrucciones load(DataRegister toRegister) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        if (this.valor instanceof DeclaracionConstante) {
            DeclaracionConstante constante = (DeclaracionConstante) this.valor;
            
            // Convertimos el valor ( sea cual sea ) a valor máquina. Ahora mismo los literales son Bool e Integer.
            // Falta por ver como se manejan los strings. De momento los dejo de lado.
            if (Tipo.Integer.equals(constante.getTipo().getTipo())) {
                Integer numero = (Integer)constante.getValor();
                bI.add(new Instruccion(OpCode.MOVE, Size.W, Literal.__(numero), toRegister));
            } else if (Tipo.Boolean.equals(constante.getTipo().getTipo())) {
            	
                int valor = mapBooleanValue((String) constante.getValor());
                bI.add(new Instruccion(OpCode.MOVE, Size.W, Literal.__(valor), toRegister));
                
            } else if (Tipo.String.equals(constante.getTipo().getTipo())) {
            	
            }
        } else {
            // Si no es una constante es una variable
        	bI.add(this.putActivationBlockAddressInRegister());
            bI.add(new Instruccion(OpCode.MOVE, Size.W, Indireccion.__(this.valor.getDesplazamiento(), AddressRegister.A6), toRegister));
        }
        return bI;
    }

    public BloqueInstrucciones createDescriptor(DataRegister DX, AddressRegister AX) {
        int size;

        if (this.valor instanceof DeclaracionConstante) {
            size = ((String)((DeclaracionConstante) this.valor).getValor()).length();
        } else {
            size = ((DeclaracionArray)this.valor).getLongitudArray();
        }

        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(new Instruccion(OpCode.JSR, new OperandoEspecial("DMMALLOC")));
        bI.add(new Instruccion(OpCode.CLR, Size.L, DX));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Literal.__(size), DX));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A0, AX));
        return bI;
    }

    public BloqueInstrucciones loadStringDescriptorConstante(DataRegister DX, AddressRegister AX) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
    	
        DeclaracionConstante constante = (DeclaracionConstante) this.getValor();
        String text = (String) constante.getValor();
        int size = text.length();
        bI.add(new Instruccion(OpCode.MOVEM, Size.L, Restore.__(AddressRegister.A0), PreDecremento.__(StackPointer.A7)));
        bI.add(createDescriptor(DX, AX));
        for (int idx = 0; idx < size; idx++) {
	        bI.add(new Instruccion(OpCode.MOVE, Size.W, Literal.__((int)text.charAt(idx)), PostIncremento.__(AddressRegister.A0)));
        }
        bI.add(new Instruccion(OpCode.MOVEM, Size.L, PostIncremento.__(StackPointer.A7), Restore.__(AddressRegister.A0)));
        
        return bI;
    }
    
    public BloqueInstrucciones loadStringDescriptorVariable(AddressRegister AX) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Literal.__(0), AddressRegister.A6));
        bI.add(this.putActivationBlockAddressInRegister());
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, AX));
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(this.getValor().getDesplazamiento()), AX));
        return bI;
    }
    /**
     * This is used just for the array initialization.
     * String initialization takes place in the assignment of a literal. This cool feature
     * is not present for the arrays at the time being. Thus, we need another
     * way of initializing it.
     */
    public BloqueInstrucciones assignDynamicMemory() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(new Instruccion(OpCode.MOVEM, Size.L, Restore.__(DataRegister.D0, AddressRegister.A0, AddressRegister.A6), PreDecremento.__(StackPointer.A7)));
        bI.add(createDescriptor(DataRegister.D0, AddressRegister.A0));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Literal.__(0), AddressRegister.A6));
        bI.add(this.putActivationBlockAddressInRegister());
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(this.getValor().getDesplazamiento()), AddressRegister.A6));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, DataRegister.D0, Contenido.__(AddressRegister.A6)));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A0, Indireccion.__(4, AddressRegister.A6)));
        bI.add(new Instruccion(OpCode.MOVEM, Size.L, PostIncremento.__(StackPointer.A7), Restore.__(DataRegister.D0, AddressRegister.A0, AddressRegister.A6)));
        return bI;
    }

    /**
     * Posibilidades:
     * Variable
     * Posicion de array
     *
     * @param fromRegister
     * @return
     */
    public BloqueInstrucciones save(CodigoMaquina.Operando fromRegister) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        // Estos dos son descriptores de variables dinámicas
        if (Tipo.String.equals(this.valor.getTipo().getTipo()) || Tipo.Array.equals(this.valor.getTipo().getTipo())) {
        	bI.add(this.putActivationBlockAddressInRegister());
        	bI.add(new Instruccion(OpCode.MOVE, Size.L, fromRegister, Indireccion.__(this.getValor().getDesplazamiento(), AddressRegister.A6)));
        } else {
        	bI.add(this.putActivationBlockAddressInRegister());
        	bI.add(new Instruccion(OpCode.MOVE, Size.W, fromRegister, Indireccion.__(this.getValor().getDesplazamiento(), AddressRegister.A6)));
        }
        return bI;
    }
    
    /*
     * 1000 BP XXXX
     * 1004 STRING #
     * 1008 STRING @
     * 100C
     */
    public BloqueInstrucciones saveStringDescriptorConstante(DataRegister DX, AddressRegister AX) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AX, AddressRegister.A0));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Literal.__(0), AddressRegister.A6));
        bI.add(this.putActivationBlockAddressInRegister());
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, AddressRegister.A1));
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(this.getValor().getDesplazamiento()), AddressRegister.A1));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, DX, Contenido.__(AddressRegister.A1)));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A0, Indireccion.__(4, AddressRegister.A1)));
        return bI;
    }
    
    public BloqueInstrucciones saveStringDescriptorVariable(AddressRegister AX) {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AX, AddressRegister.A0));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Literal.__(0), AddressRegister.A6));
        bI.add(this.putActivationBlockAddressInRegister());
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, AddressRegister.A1));
		bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(this.getValor().getDesplazamiento()), AddressRegister.A1));
		bI.add(new Instruccion(OpCode.MOVE, Size.L, Contenido.__(AddressRegister.A0), Contenido.__(AddressRegister.A1)));
		bI.add(new Instruccion(OpCode.MOVE, Size.L, Indireccion.__(4, AddressRegister.A0), Indireccion.__(4, AddressRegister.A1)));
        return bI;
    }

    public Declaracion getValor() {
        return valor;
    }

    public int getProfundidad() {
        return profundidad;
    }

    private int mapBooleanValue(String value) {
        return value.equals("true") ? 1 : 0;
    }

}
