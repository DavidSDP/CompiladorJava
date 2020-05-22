package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.Indireccion;
import CodigoMaquina.especiales.Literal;

public class DeclaracionIndireccion extends InstruccionTresDirecciones {
    
    public DeclaracionIndireccion(Operando array) {
        super(OperacionTresDirecciones.DECLARAR_INDIRECCION);
        this.primero = array;
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.primero.assignDynamicMemory(AddressRegister.A0));
        bI.add(new Instruccion(OpCode.MOVE, Size.W, Literal.__(1), Indireccion.__(2, AddressRegister.A0)));
        this.primero.getValor().markAsInitialized();
        return bI.toString();
    }
}
