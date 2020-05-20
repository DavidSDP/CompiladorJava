package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.Literal;
import Procesador.Declaracion;

/**
 * This class is intended to push strings and arrays in the stack
 * when calling a function
 */
public class ComplexParam extends Param {

    public ComplexParam(Operando primero) {
        super(OperacionTresDirecciones.COMPLEX_PARAM, primero);
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.primero.load(DataRegister.D0));
        Declaracion valor = this.primero.getValor();
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(valor.getDesplazamiento()), AddressRegister.A6));
        bI.add(Instruccion.nuevaInstruccion("\tPUSH_DESCRIPTOR_PARAM A6\n"));
        return bI.toString();
    }
}
