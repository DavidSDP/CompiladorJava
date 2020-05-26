package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;

/**
    TODO Aclarar que pasa con este tipo de operaciones en los condicionales
    Es decir, if (a<b) probablemente deba utilizar una variable temporal
    donde se guarde el valor de la comparacion se pueda usar en este punto.
*/
public class And extends InstruccionTresDirecciones {    
    public And(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.AND);
        this.setPrimero(primero);
        this.setSegundo(segundo);
        this.setTercero(tercero);
    }

    /**
     * La estrategia para calcular la and es la siguiente:
     * 1. Mover primer valor a D0
     * 2. Mover segundo valor a D1
     * 3. Hacer and logica y guardar el valor en D1
     * 4. Mover el valor de D1 al tercer operando
     *
     * Ejemplo:
     *      { Codigo carga de @BA de primero en A6 }
     *      move.b 10(A6), D0
     *      { Codigo carga de @BA de segundo en A6 }
     *      move.b 18(A6), D0
     *      { Codigo carga de @BA de tercero en A6 }
     *      move.b 18(A6), D0
     *
     * Una posible optimización sería detectar si alguno de los operandos es el destino y
     * ordenar las instrucciones en consecuencia. Nos ahorrariamos tener que buscar el bloque de activación
     * de nuevo!
     *
     */
    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.getPrimero().load(DataRegister.D0));
        bI.add(this.getSegundo().load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.AND, DataRegister.D0, DataRegister.D1));
        bI.add(this.getTercero().save(DataRegister.D1));
        return bI.toString();
    }
}
